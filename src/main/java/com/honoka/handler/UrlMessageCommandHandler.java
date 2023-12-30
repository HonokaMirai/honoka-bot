package com.honoka.handler;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.honoka.api.SzfxApi;
import com.honoka.api.YoutubeApi;
import com.honoka.api.resp.youtube.YoutubeVideoInfoPojo;
import com.honoka.config.BotConfig;
import com.honoka.config.FileConfig;
import com.honoka.config.UrlMessageConfig;
import com.honoka.api.resp.BilibiliVideoSzfxEntity;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/**
 * @author honoka
 * @version 1.0
 * @description
 * @date 2023/7/17 18:42
 */
public class UrlMessageCommandHandler {


    /**
     * 解析链接消息
     *
     * @param sender
     * @param messageChain
     */
    public static void parseUrlMessage(Member sender, MessageChain messageChain) {
        Group group = sender.getGroup();
        String message = messageChain.contentToString();
        // 构建返回消息
        MessageChainBuilder builder = new MessageChainBuilder()
                .append(new QuoteReply(messageChain));
        String content = "";

        if (message.contains(UrlMessageConfig.Constant.BILIBILI) || message.contains(UrlMessageConfig.Constant.BTV)) {
            // 判断该连接是否为Bilibili链接
            content = handleBilibiliUrlMessage(sender, messageChain, builder);
        } else if (message.contains(UrlMessageConfig.Constant.YOUTUBE) || message.contains(UrlMessageConfig.Constant.YOUTU_BE)) {
            // 判断该连接是否为Youtube链接
            content = handleYoutubeUrlMessage(sender, messageChain, builder);
        } else {
            return;
        }

        if (StrUtil.isNotBlank(content)) {
            // 发送返回消息
            MessageChain sendMessage = builder
                    .append(content)
                    .build();
            group.sendMessage(sendMessage);
        }

    }

    /**
     * 处理bilibili链接消息
     * @param sender
     * @param messageChain
     * @param builder
     * @return
     */
    private static String handleBilibiliUrlMessage(Member sender, MessageChain messageChain, MessageChainBuilder builder) {
        // 判断是否为bilibili视频
        String content = messageChain.contentToString();
        if (content.contains(UrlMessageConfig.Constant.BILIBILI) && !content.contains(UrlMessageConfig.Constant.VIDEO)) {
            return "";
        }
        // 调用松鼠API
        BilibiliVideoSzfxEntity entity = SzfxApi.getBilibiliVideoInfo(content);
        if (Objects.equals(entity.getCode(), UrlMessageConfig.Code.SUCCESS)) {
            // 压缩视频封面
            File compressFile = null;
            try {
                compressFile = FileConfig.compressImageFile(new URL(entity.getCover()), 250);
            } catch (MalformedURLException e) {
                BotConfig.logger.error("URL解析失败", e);
            }
            // 将previewUrl转换成File对象
            if (Objects.nonNull(compressFile)) {
                Image compressImage = Contact.uploadImage(sender.getGroup(), compressFile);
                builder.append(compressImage).append("\n");
            }
            if (StrUtil.isBlank(entity.getTitle()) && StrUtil.isBlank(entity.getDesc())) {
                return "";
            }
            // 拼接返回消息
            return "视频标题: " + entity.getTitle() + "\n" +
                    "视频简介: " + entity.getDesc() + "\n";

        }
        return "";
    }

    /**
     * 处理Youtube链接消息
     * @param sender
     * @param messageChain
     * @param builder
     * @return
     */
    private static String handleYoutubeUrlMessage(Member sender, MessageChain messageChain, MessageChainBuilder builder) {
        // 获取视频连接中的id
        UrlBuilder urlBuilder = UrlBuilder.of(messageChain.contentToString(), CharsetUtil.CHARSET_UTF_8);
        String videoId = urlBuilder.getQuery().get("v").toString();
        // 调用youtubeAPI
        YoutubeVideoInfoPojo pojo = YoutubeApi.getVideoList(videoId);
        String title = pojo.getTitle();
        String thumbnails = pojo.getThumbnails();
        // 构建返回消息
        InputStream inputStream = null;
        try {
            if (Objects.nonNull(thumbnails)) {
                inputStream = new URL(URLUtil.normalize(thumbnails)).openConnection().getInputStream();
                Image image = Contact.uploadImage(sender.getGroup(), inputStream);
                builder.append(image).append("\n");
            }
        } catch (Exception e) {
            BotConfig.logger.error("上传图片失败", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                BotConfig.logger.error("流关闭失败", e);
            }
        }
        return title;
    }
}
