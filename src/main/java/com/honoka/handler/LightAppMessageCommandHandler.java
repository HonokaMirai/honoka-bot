package com.honoka.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSONObject;
import com.honoka.config.BotConfig;
import com.honoka.mirai.entity.LightAppEntity;
import com.honoka.mirai.entity.LightAppMetaDetail;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

/**
 * @author honoka
 * @version 1.0
 * @description
 * @date 2023/7/17 15:17
 */
public class LightAppMessageCommandHandler {

    /**
     * 解析小程序消息
     *
     * @param sender
     * @param message
     */
    public static void parseLightAppMessage(Member sender, SingleMessage message) {
        // 将该小程序消息转换成LightApp类型
        Group group = sender.getGroup();
        LightApp lightApp = (LightApp) message;
        String content = lightApp.getContent();
        // 将该小程序转成对象
        LightAppEntity lightAppEntity = JSONObject.parseObject(content, LightAppEntity.class);
        if (Objects.isNull(lightAppEntity)) {
            BotConfig.logger.info("小程序消息解析失败");
        }

        LightAppMetaDetail detail = lightAppEntity.getMeta().getDetail1();
        String previewUrl = detail.getPreview();
        // 拼接返回消息
        StringBuilder sb = new StringBuilder();
        sb.append(detail.getTitle()).append(" - ").append(detail.getDesc()).append("\n")
                .append(detail.getQqDocUrl().split("\\?")[0]).append("\n");

        // 将previewUrl转换成File对象
        MessageChainBuilder builder = new MessageChainBuilder();
        InputStream inputStream = null;
        try {
            if (StrUtil.isNotBlank(previewUrl)) {
                inputStream = new URL(URLUtil.normalize(previewUrl)).openConnection().getInputStream();
                Image preview = Contact.uploadImage(group, inputStream);
                builder.append(preview).append("\n");
            }
        } catch (IOException e) {
            BotConfig.logger.error("上传图片失败", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                BotConfig.logger.error("流关闭失败", e);
            }
        }

        // 发送返回消息
        MessageChain sendMessage = builder
                .append(sb.toString())
                .build();
        group.sendMessage(sendMessage);
    }

}
