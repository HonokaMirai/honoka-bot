package com.honoka.dao;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.function.Predicate;

/**
 * @author
 * @className UpdateBatchSomeColumSelectiveById
 * @description update user set name = "a", age = 17 where id = 1;
 * update user set name = "b", age = 18 where id = 2;
 * <script>
 * <foreach collection="list" item="item" separator=";">
 * update user
 * <set>
 * <if test="item.name != null and item.name != ''">
 * name = #{item.name,jdbcType=VARCHAR},
 * </if>
 * <if test="item.age != null">
 * age = #{item.age,jdbcType=INTEGER},
 * </if>
 * </set>
 * where id = #{item.id,jdbcType=INTEGER}
 * </foreach>
 * </script>
 * @date
 */
@Slf4j
public class UpdateBatchSomeColumnSelectiveById extends AbstractMethod {
    /**
     * 方法名称
     */
    private static final String METHOD_NAME = "updateBatchSomeColumnSelectiveById";

    /**
     * 默认方法名
     */
    public UpdateBatchSomeColumnSelectiveById() {
        super(METHOD_NAME);
    }

    /**
     * 默认方法名
     *
     * @param predicate 字段筛选条件
     */
    public UpdateBatchSomeColumnSelectiveById(Predicate<TableFieldInfo> predicate) {
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
     * @date 2023-01-30 09:43:04
     */
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.UPDATE_BY_ID;
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();

        final String additional = optlockVersion(tableInfo) + tableInfo.getLogicDeleteSql(true, true);
        String sqlSet = this.filterTableFieldInfo(fieldList, getPredicate(),
                i -> i.getSqlSet(false, ENTITY_DOT), NEWLINE);
        sqlSet = SqlScriptUtils.convertSet(sqlSet);
        //log.debug("sqlSet:{}", sqlSet);
        String sql = String.format("UPDATE %s %s WHERE %s=#{%s} %s", tableInfo.getTableName(), sqlSet,
                tableInfo.getKeyColumn(), ENTITY_DOT + tableInfo.getKeyProperty(), additional);

        //log.debug("sql:{}", sql);
        sql = SqlScriptUtils.convertForeach(sql, Constants.LIST, null, ENTITY, SEMICOLON);
        // 添加<script>标签
        sql = String.format("<script>\n%s\n</script>", sql);
        //log.debug("sql:{}", sql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, modelClass, sqlSource);
    }

    private Predicate<TableFieldInfo> getPredicate() {
        Predicate<TableFieldInfo> noLogic = t -> !t.isLogicDelete();
        if (predicate != null) {
            return noLogic.and(predicate);
        }
        return noLogic;
    }
}
