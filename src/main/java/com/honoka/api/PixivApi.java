package com.honoka.api;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.honoka.api.resp.PixivDataResp;
import com.honoka.api.resp.PixivResp;
import com.honoka.config.BotConfig;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 *
 */
public class PixivApi {

    /**
     * 能用 HTTP 的就用 HTTP，HTTPS 性能比较低，实验比较大
     */
    public static final String pidApi = "https://api.lolicon.app/setu/v2";
    public static final String beautifulImageUrl = "https://api.sunweihu.com/api/sjbz/api.php";
    public static final String chickenSoupUrl = "https://api.shadiao.app/chp/";
    public static final String duChickenSoupUrl = "https://api.shadiao.app/du";
    public static final String pixivSearchApi = "http://pximg.rainchan.win/img?img_id=IMGID";
    public static final String randomUrl = "http://pximg.rainchan.win/img";


    public static List<String> getImageList(List<String> tags) throws IOException {
        StringBuilder url = new StringBuilder(pidApi);
        return getPixivImageList(tags, url);
    }

    public static List<String> getR18ImageList(List<String> tags) throws IOException {
        StringBuilder url = new StringBuilder(pidApi + "?r18=1");
        return getPixivImageList(tags, url);
    }

    /**
     * 根据 tag 获取p站图片地址
     * @param tags
     * @return
     * @throws IOException
     */
    public static List<String> getPixivImageList(List<String> tags, StringBuilder url) throws IOException {
        List<String> imageList = new ArrayList<>();

        if (tags != null) {
            url.append("?");
            for (String tag : tags) {
                url.append("tag=").append(URLEncoder.encode(tag, "UTF-8")).append("&");
            }
            url = new StringBuilder(url.substring(0, url.length() - 1));
        }
        BotConfig.logger.info("请求Pixiv的URL为：" + url);
        String result = HttpUtil.get(url.toString(), 3000);
        BotConfig.logger.info("请求Pixiv结果为：" + result);
        if (result != null && !"".equals(result)) {
            PixivResp pixivResponse = JSONObject.parseObject(result, PixivResp.class);
            List<PixivDataResp> dataList = pixivResponse.getData();
            if (CollectionUtil.isNotEmpty(dataList)) {
                for (PixivDataResp dataVO : dataList) {
                    JSONObject urls = dataVO.getUrls();
                    String originalUrl = (String) urls.get("original");
                    imageList.add(originalUrl);
                }
            }
        }
        return imageList;
    }
}
