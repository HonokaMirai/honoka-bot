package com.honoka.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.stream.Collectors.joining;

/**
 * @className InsertBatchSomeColumnSelective
 * @description 解决InsertBatchSomeColumn插入不过滤null情况，支持批量插入字段为空不插入
 * 注意批量插入只支持相同的语句，不支持集合中部分集合为空字段不一致的情况
 * insert into user(id, name, age) values (1, "a", 17), (2, "b", 18);
 * <script>
 *    insert into user(id, name, age) values
 *    <foreach collection="list" item="item" index="index" open="(" separator="),(" close=")">
 *       #{item.id}, #{item.name}, #{item.age}
 *    </foreach>
 * </script>
 */
@Slf4j
public class InsertBatchSomeColumnSelective extends AbstractMethod {

    /**
     * 方法名称
     */
    private static final String METHOD_NAME = "insertBatchSomeColumnSelective";

    /**
     * 默认方法名
     */
    public InsertBatchSomeColumnSelective() {
        super(METHOD_NAME);
    }

    /**
     * 默认方法名
     *
     * @param predicate 字段筛选条件
     */
    public InsertBatchSomeColumnSelective(Predicate<TableFieldInfo> predicate) {
        super(METHOD_NAME);
        this.predicate = predicate;
    }

    /**
     * 字段筛选条件
     */
    @Setter
    @Accessors(chain = true)
    private Predicate<TableFieldInfo> predicate;

    /**
     * @param mapperClass mapper类
     * @param modelClass  模型类
     * @param tableInfo   表信息
     * @return {@link MappedStatement }
     * @methodsName injectMappedStatement
     * @description 拼sql语句实现方法
     * @author ""
     * @date 2023-01-29 17:45:29
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        SqlMethod sqlMethod = SqlMethod.INSERT_ONE;
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();

        String columnScript = SqlScriptUtils.convertTrim(tableInfo.getKeyInsertSqlColumn(true, false) + fieldList.stream().map(i -> i.getInsertSqlColumnMaybeIf(Constants.LIST + LEFT_SQ_BRACKET + 0 + RIGHT_SQ_BRACKET + DOT))
                        .filter(Objects::nonNull).collect(joining(NEWLINE)),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);

        //log.debug("columnScript:{}", columnScript);
        String insertSqlProperty = SqlScriptUtils.convertTrim(tableInfo.getKeyInsertSqlProperty(true, null, true) + fieldList.stream()
                        .map(i -> i.getInsertSqlPropertyMaybeIf(ENTITY_DOT)).filter(Objects::nonNull).collect(joining(NEWLINE)),
                LEFT_BRACKET, RIGHT_BRACKET, null, COMMA);


        //log.debug("valuesScript:{}", insertSqlProperty);

        String valuesScript = SqlScriptUtils.convertForeach(insertSqlProperty, Constants.LIST, null, ENTITY, COMMA);
        //log.debug("valuesScript:{}", valuesScript);
        String keyProperty = null;
        String keyColumn = null;
        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (tableInfo.havePK()) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                /* 自增主键 */
                keyGenerator = Jdbc3KeyGenerator.INSTANCE;
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else {
                if (null != tableInfo.getKeySequence()) {
                    keyGenerator = TableInfoHelper.genKeyGenerator(METHOD_NAME, tableInfo, builderAssistant);
                    keyProperty = tableInfo.getKeyProperty();
                    keyColumn = tableInfo.getKeyColumn();
                }
            }
        }
        String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), columnScript, valuesScript);
        //log.debug("sql:{}", sql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, sqlSource, keyGenerator, keyProperty, keyColumn);
    }
}
