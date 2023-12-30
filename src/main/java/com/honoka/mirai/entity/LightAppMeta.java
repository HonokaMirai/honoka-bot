package com.honoka.mirai.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author honoka
 * @version 1.0
 * @description
 * @date 2023/7/17 15:41
 */
@Data
public class LightAppMeta {

    @JSONField(name = "detail_1")
    private LightAppMetaDetail detail1;
}
