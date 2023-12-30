package com.honoka.api.resp.youtube;

import lombok.Data;

@Data
public class YoutubeVideoInfoPojo {

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频封面
     */
    private String thumbnails;
}
