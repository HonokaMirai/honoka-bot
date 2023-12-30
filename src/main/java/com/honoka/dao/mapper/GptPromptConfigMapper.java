package com.honoka.dao.mapper;

import com.honoka.dao.EasyBaseMapper;
import com.honoka.dao.entity.GptPromptConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mybatis-plus
 * @since 2023-07-12
 */
@Mapper
public interface GptPromptConfigMapper extends EasyBaseMapper<GptPromptConfig> {

    GptPromptConfig selectConfigByType(String type);
}
