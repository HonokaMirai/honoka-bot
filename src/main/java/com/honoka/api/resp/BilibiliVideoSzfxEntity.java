package com.honoka.api.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author honoka
 * @version 1.0
 * @description B站视频松鼠API所用实体
 * @date 2023/7/17 18:51
 */
@Data
public class BilibiliVideoSzfxEntity {

    private String code;

    private String title;

    @JSONField(name = "bvid")
    private String bvId;

    private String aid;

    private String cover;

    @JSONField(name = "upname")
    private String upName;

    @JSONField(name = "upavatar")
    private String upAvatar;

    private String desc;


}
