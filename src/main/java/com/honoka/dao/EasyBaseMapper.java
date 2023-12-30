package com.honoka.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @className EasyBaseMapper
 */
public interface EasyBaseMapper<T> extends BaseMapper<T> {
    /**
     * 批量插入
     *
     * @param entityList 实体列表
     * @return {@link Integer }
     * @author ""
     * @description 批量插入 仅适用于MysqL，过滤表字段为null的不插入
     * @methodsName insertBatchSomeColumnSelective
     * @date
     */
    Integer insertBatchSomeColumnSelective(List<T> entityList);

    /**
     * 分批次批量插入数据
     *
     * @param entityList
     * @return
     */
    default Integer insertBatchSomeColumnSelectivePartition(List<T> entityList) {
        AtomicInteger integer = new AtomicInteger(0);
        List<List<T>> parts = Lists.partition(entityList, 100);
        parts.forEach(list -> {
            integer.updateAndGet(v -> v + insertBatchSomeColumnSelective(list));
        });
        return integer.get();
    }

    /**
     * 根据主键id批量更新 拼接分号分割的sql，过滤表字段为null的不更新
     *
     * @param entityList 实体列表
     * @return {@link Integer }
     * @methodsName updateBatchSomeColumnSelectiveById
     * @description 根据主键id批量更新 拼接分号分割的sql，过滤表字段为null的不更新
     * @author ""
     * @date
     */
    Integer updateBatchSomeColumnSelectiveById(List<T> entityList);

    /**
     * 分批次更新 CommonConstant.BATCH定义批次大小
     *
     * @param entityList 实体列表
     * @return {@link Integer }
     * @methodsName updateBatchSomeColumnSelectiveByIdPartition
     * @description 分批次更新 CommonConstant.BATCH定义批次大小
     * @author ""
     * @date
     */
    default Integer updateBatchSomeColumnSelectiveByIdPartition(List<T> entityList) {
        AtomicInteger integer = new AtomicInteger(0);
        List<List<T>> parts = Lists.partition(entityList, 100);
        parts.forEach(list -> {
            integer.updateAndGet(v -> v + updateBatchSomeColumnSelectiveById(list));
        });
        return integer.get();
    }
}
