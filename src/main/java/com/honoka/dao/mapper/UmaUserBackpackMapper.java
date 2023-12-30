package com.honoka.dao.mapper;

import com.honoka.dao.dto.UserUmaPackage;
import com.honoka.dao.entity.UmaUserBackpack;
import com.honoka.dao.EasyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2023-07-16
 */
@Mapper
public interface UmaUserBackpackMapper extends EasyBaseMapper<UmaUserBackpack> {

    /**
     * 批量插入或更新用户背包
     * @param gachaResultList
     * @return
     */
    int batchInsertOrUpdateGachaList(@Param("list") List<UserUmaPackage> gachaResultList);
}
