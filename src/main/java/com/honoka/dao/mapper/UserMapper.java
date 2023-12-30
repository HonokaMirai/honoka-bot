package com.honoka.dao.mapper;

import com.honoka.dao.entity.User;
import com.honoka.dao.EasyBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2023-07-16
 */
@Mapper
public interface UserMapper extends EasyBaseMapper<User> {

    int updateUserDiamond(@Param("qq") Long qq, @Param("type") String type, @Param("diamond") Integer diamond);
}
