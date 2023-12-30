package com.honoka.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.honoka.config.BotConfig;
import com.honoka.dao.EasyBaseMapper;
import com.honoka.dao.EasyCustomSqlInjector;
import com.honoka.mirai.config.DatabaseMybatisConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class MybatisUtil {

    public static volatile SqlSessionFactory sqlSessionFactory;

    public MybatisUtil() {}

    /**
     * 初始化sqlSessionFactory
     */
    public static void init() {
        MiraiUtil.changeClassLoader(() -> {
            // 这是mybatis-plus的配置对象，对mybatis的Configuration进行增强
            MybatisConfiguration mybatisConfiguration = initMybatisPlusConfiguration();
            // 初始化DruidDataSource
            DataSource dataSource = initDruidDataSource();
            // 生成Environment对象
            Environment environment = new Environment("development", new JdbcTransactionFactory(), dataSource);
            mybatisConfiguration.setEnvironment(environment);
            // 解析mapper.xml文件
            //try {
            //    registryMapperXml(mybatisConfiguration, "mapper");
            //} catch (Exception e) {
            //    BotConfig.logger.error("解析mapper.xml文件时出现异常", e);
            //}
            // 生成sqlSessionFactory对象
            //sqlSessionFactory = new MybatisSqlSessionFactoryBuilder().build(mybatisConfiguration);
            MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            sqlSessionFactoryBean.setConfiguration(mybatisConfiguration);
//            DatabaseMybatisConfig.INSTANCE.MAPPER_INTERFACE.set("com.honoka.dao.mapper");
//            DatabaseMybatisConfig.INSTANCE.MAPPER_XML.set("classpath:mapper/*.xml");
            sqlSessionFactoryBean.setTypeAliasesPackage(DatabaseMybatisConfig.INSTANCE.MAPPER_INTERFACE.get());
            try {
                Resource[] resource  = new PathMatchingResourcePatternResolver().getResources(DatabaseMybatisConfig.INSTANCE.MAPPER_XML.get());
                sqlSessionFactoryBean.setMapperLocations(resource);
                sqlSessionFactoryBean.setGlobalConfig(initGlobalConfig(mybatisConfiguration));
                sqlSessionFactory = sqlSessionFactoryBean.getObject();
            } catch (Exception e) {
                BotConfig.logger.error("初始化sqlSessionFactory时出现异常", e);
            }
        });

    }

    /**
     * 初始化MybatisPlusConfiguration
     */
    private static MybatisConfiguration initMybatisPlusConfiguration() {
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        // 开启驼峰大小写转换
        mybatisConfiguration.setMapUnderscoreToCamelCase(true);
        // 配置添加数据自动返回数据主键
        mybatisConfiguration.setUseGeneratedKeys(true);
        // 这是初始化连接器，如mybatis-plus的分页插件
        mybatisConfiguration.addInterceptor(initInterceptor());
        // 配置日志实现
        mybatisConfiguration.setLogImpl(Slf4jImpl.class);
        // 扫描mapper接口所在包
        //mybatisConfiguration.addMappers("com.honoka.dao.mapper");
        return mybatisConfiguration;
    }

    /**
     * 初始化GlobalConfig
     * @param mybatisConfiguration
     * @return
     */
    public static GlobalConfig initGlobalConfig(MybatisConfiguration mybatisConfiguration) {
        // 构建mybatis-plus需要的globalconfig
        GlobalConfig globalConfig = GlobalConfigUtils.getGlobalConfig(mybatisConfiguration);
        // 此参数会自动生成实现baseMapper的基础方法映射，添加自定义SQL注入器
        globalConfig.setSqlInjector(new EasyCustomSqlInjector());
        // 设置id生成器
        globalConfig.setIdentifierGenerator(new DefaultIdentifierGenerator());
        // 设置超类mapper
        globalConfig.setSuperMapperClass(EasyBaseMapper.class);
        return globalConfig;
    }

    /**
     * 初始化DruidDataSource
     * @return
     */
    private static DataSource initDruidDataSource() {
//        DatabaseMybatisConfig.INSTANCE.URL.set("jdbc:mysql://124.222.20.2:3306/honoka_bot?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC");
//        DatabaseMybatisConfig.INSTANCE.USERNAME.set("honoka");
//        DatabaseMybatisConfig.INSTANCE.PASSWORD.set("Aa5115548!");
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(DatabaseMybatisConfig.INSTANCE.DRIVER.get());
        dataSource.setUrl(DatabaseMybatisConfig.INSTANCE.URL.get());
        dataSource.setUsername(DatabaseMybatisConfig.INSTANCE.USERNAME.get());
        dataSource.setPassword(DatabaseMybatisConfig.INSTANCE.PASSWORD.get());
        // 设置其他属性
        dataSource.setInitialSize(DatabaseMybatisConfig.INSTANCE.INITIAL_SIZE.get());
        dataSource.setMinIdle(DatabaseMybatisConfig.INSTANCE.MIN_IDLE.get());
        dataSource.setMaxActive(DatabaseMybatisConfig.INSTANCE.MAX_ACTIVE.get());
        dataSource.setMaxWait(DatabaseMybatisConfig.INSTANCE.MAX_WAIT.get());
        return dataSource;
    }

    /**
     * 初始化拦截器
     * @return
     */
    private static Interceptor initInterceptor() {
        //创建mybatis-plus插件对象
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //构建分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        paginationInnerInterceptor.setOverflow(true);
        paginationInnerInterceptor.setMaxLimit(500L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        //乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * 解析mapper.xml文件
     * @param configuration
     * @param classPath
     * @throws IOException
     */
    private static void registryMapperXml(MybatisConfiguration configuration, String classPath) throws IOException {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> mapper = contextClassLoader.getResources(classPath);
        while (mapper.hasMoreElements()) {
            URL url = mapper.nextElement();

            if (url.getProtocol().equals("file")) {
                String path = url.getPath();
                File file = new File(path);
                File[] files = file.listFiles();
                for (File f : files) {
                    FileInputStream in = new FileInputStream(f);
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, f.getPath(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                    in.close();
                }
            } else {
                JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
                JarFile jarFile = urlConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    if (jarEntry.getName().endsWith(".xml")) {
                        BotConfig.logger.info("jarEntry.getName()为：" + jarEntry.getName());
                        InputStream in = jarFile.getInputStream(jarEntry);
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, jarEntry.getName(), configuration.getSqlFragments());
                        xmlMapperBuilder.parse();
                        in.close();
                    }
                }
            }
        }
    }

    /**
     * 获取SqlSessionFactory
     * @return
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            synchronized (MybatisUtil.class) {
                if (sqlSessionFactory == null) {
                    try{
                        // 初始化
                        init();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sqlSessionFactory;
    }

    /**
     * 获取SqlSession
     * @return
     */
    public static SqlSession getSqlSession() {
        return getSqlSessionFactory().openSession();
    }

    /**
     * 获取Mapper
     * @param mapperClass
     * @param <T>
     * @return
     */
    public static <T> T getMapper(Class<T> mapperClass) {
        SqlSession sqlSession = getSqlSession();
        return sqlSession.getMapper(mapperClass);
    }

    /**
     * 执行SqlSessionFunction
     * @param function
     * @param <T>
     * @return
     */
    public static <T> T execute(SqlSessionFunction<T> function) {
        try (SqlSession sqlSession = getSqlSession()) {
            T result = function.apply(sqlSession);
            sqlSession.commit();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @FunctionalInterface
    public interface SqlSessionFunction<T> {
        T apply(SqlSession sqlSession);
    }

}
