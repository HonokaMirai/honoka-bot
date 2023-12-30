package com.honoka.api;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.honoka.api.resp.youtube.YoutubeCommonResp;
import com.honoka.api.resp.youtube.YoutubeVideoInfoPojo;
import com.honoka.api.resp.youtube.YoutubeVideoItem;
import com.honoka.api.resp.youtube.YoutubeVideoSnippet;
import com.honoka.config.BotConfig;
import com.honoka.mirai.config.YoutubeApiConfig;

public class YoutubeApi {

    public static final String VIDEO_URL = "https://www.googleapis.com/youtube/v3/videos?part=snippet";

    public static final String KEY = YoutubeApiConfig.INSTANCE.API_KEY.get();


    /**
     * 获取单个Youtube视频信息
     * @param videoId 视频id
     * @return
     */
    public static YoutubeVideoInfoPojo getVideoList(String videoId) {
        // 拼接url
        String url = VIDEO_URL + "&key=" + KEY + "&id=" + videoId;
        // 请求url
        BotConfig.logger.info("YoutubeApi请求的url为：" + url);
        String body = HttpRequest.get(url).execute().body();
        // 解析json
        YoutubeCommonResp youtubeCommonResp = JSONObject.parseObject(body, YoutubeCommonResp.class);
        // 返回
        if (youtubeCommonResp != null) {
            YoutubeVideoSnippet snippet = youtubeCommonResp.getItems().get(0).getSnippet();
            YoutubeVideoInfoPojo pojo = new YoutubeVideoInfoPojo();
            pojo.setTitle(snippet.getTitle());
            pojo.setThumbnails(snippet.getThumbnails().getMedium().getUrl());
            return pojo;
        }
        return null;
    }
}
