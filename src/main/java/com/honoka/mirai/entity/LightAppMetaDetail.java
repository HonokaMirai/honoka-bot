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
public class LightAppMetaDetail {

    private String appid;

    private String preview;

    private Object shareTemplateData;

    private String gamePointsUrl;

    private String gamePoints;

    private String url;

    private Integer appType;

    private String desc;

    private String title;

    private Integer scene;

    private Object host;

    private String icon;

    private String shareTemplateId;

    @JSONField(name = "qqdocurl")
    private String qqDocUrl;

    private String showLittleTail;
}
