package com.honoka.api;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.honoka.api.resp.BilibiliVideoSzfxEntity;
import com.honoka.api.resp.NetEaseMusicSzfxEntity;
import net.mamoe.mirai.message.data.MusicKind;
import net.mamoe.mirai.message.data.MusicShare;

/**
 * 松鼠API
 */
public class SzfxApi {

    /**
     * 松鼠API-获取B站视频信息
     */
    public static final String SZFX_BILIBILI_URL = "https://api.szfx.top/bilibili/api.php";

    public static final String SZFX_NETEASE_MUSIC_URL = "https://api.szfx.top/music163/";

    /**
     * 获取B站视频信息
     * @param url
     * @return
     */
    public static BilibiliVideoSzfxEntity getBilibiliVideoInfo(String url) {
        // 调用松鼠API
        String body = HttpRequest.get(SzfxApi.SZFX_BILIBILI_URL).form("url", url).execute().body();
        // 调用松鼠API成功
        return JSONObject.parseObject(body, BilibiliVideoSzfxEntity.class);
    }

    /**
     * 获取网易云音乐信息
     * @param song
     * @return
     */
    public static NetEaseMusicSzfxEntity getNetEaseMusic(String song) {
        // 调用松鼠API
        String body = HttpRequest.get(SzfxApi.SZFX_NETEASE_MUSIC_URL).form("song", song).form("order", 0).execute().body();
        // 调用松鼠API成功
        return JSONObject.parseObject(body, NetEaseMusicSzfxEntity.class);
    }

    public static String build() {
        new MusicShare(
                MusicKind.NeteaseCloudMusic,
                "ジェリーフィッシュ",
                "Yunomi/ローラーガール",
                "https://y.music.163.com/m/song?id=562591636&uct=QK0IOc%2FSCIO8gBNG%2Bwcbsg%3D%3D&app_version=8.7.46",
                "http://p1.music.126.net/KaYSb9oYQzhl2XBeJcj8Rg==/109951165125601702.jpg",
                "http://music.163.com/song/media/outer/url?id=562591636&&sc=wmv&tn=",
                "[分享]ジェリーフィッシュ"
                );
        return null;
    }

}
