package com.honoka.api;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.honoka.api.resp.waifu.WaifuResp;

import java.util.List;

public class WaifuApi {

    public static final String WAIFU_URL = "https://api.waifu.pics/nsfw/";

    public static final String WAIFU = "waifu";
    public static final String NEKO = "neko";
    public static final String TRAP = "trap";
    public static final String BLOWJOB = "blowjob";

    public static final List<String> CATEGORY_LIST = Lists.newArrayList(WAIFU, NEKO, TRAP, BLOWJOB);

    public static String getImage() {
        // 从CATEGORY_LIST中获取一个随机元素
        int index = RandomUtil.randomInt(0, 3);
        String body = HttpRequest.get(WAIFU_URL + CATEGORY_LIST.get(index)).execute().body();
        WaifuResp resp = JSONObject.parseObject(body, WaifuResp.class);
        return resp.getUrl();
    }
}
