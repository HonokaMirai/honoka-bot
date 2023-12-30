package com.honoka.mirai.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Objects;

/**
 * @author honoka
 * @version 1.0
 * @description 小程序对象实体
 * @date 2023/7/17 15:35
 */
@Data
public class LightAppEntity {

    private Object config;

    private String prompt;

    private String app;

    private String ver;

    @JSONField(name = "appID")
    private String appId;

    private String view;

    private LightAppMeta meta;

    @JSONField(name = "bthirdappforward")
    private Boolean bThirdAppForward;

    @JSONField(name = "bthirdappforwardforbackendswitch")
    private Boolean bThirdAppForwardForBackEndSwitch;

    private String desc;
}
