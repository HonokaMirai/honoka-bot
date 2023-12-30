package com.honoka.mirai.config;

import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.data.java.JavaAutoSavePluginConfig;
import org.jetbrains.annotations.NotNull;

/**
 * @description 数据库Mybatis配置类，用于配置DataSource、Mybatis、Mybatis-plus的相关信息
 * @author Honoka
 */
public class DatabaseMybatisConfig extends JavaAutoSavePluginConfig {
    public static final DatabaseMybatisConfig INSTANCE = new DatabaseMybatisConfig("DatabaseMybatisConfig");

    public DatabaseMybatisConfig(@NotNull String saveName) {
        super(saveName);
    }

    /**
     * @description Datasource相关配置
     */
    public final Value<String> DRIVER = value("driver", "com.mysql.cj.jdbc.Driver");

    public final Value<String> URL = value("url", "jdbc:mysql://localhost:3306/database?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC");

    public final Value<String> USERNAME = value("username", "root");

    public final Value<String> PASSWORD = value("password", "password");

    public final Value<Integer> INITIAL_SIZE = value("initial_size", 1);

    public final Value<Integer> MIN_IDLE = value("min_idle", 1);

    public final Value<Integer> MAX_ACTIVE = value("max_active", 20);

    public final Value<Integer> MAX_WAIT = value("max_wait", 6000);

    /**
     * mapper接口所在包名
     */
    public final Value<String> MAPPER_INTERFACE = value("mapper_interface", "com.xxx.dao.mapper");

    /**
     * mapper.xml文件在resources下所在包名
     */
    public final Value<String> MAPPER_XML = value("mapper_xml", "mapper/**/*.xml");

}
