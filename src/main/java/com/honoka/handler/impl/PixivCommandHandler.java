package com.honoka.handler.impl;

import cn.hutool.core.img.ImgUtil;
import com.honoka.api.PixivApi;
import com.honoka.config.CommandConfig;
import com.honoka.handler.CommandHandler;
import com.honoka.util.CommandUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.SingleMessage;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 获取pixiv图片
 * @author honoka
 * @data 2023-02-02 00:42:02
 */
public class PixivCommandHandler implements CommandHandler {


    @Override
    public void handle(GroupMessageEvent groupMessageEvent, String command) {
        //处理源消息
        MessageChain message = groupMessageEvent.getMessage();
        for (SingleMessage singleMessage : message) {
            String content = singleMessage.contentToString();

            if (StringUtils.isNotBlank(content)) {
                List<String> tags = CommandUtil.commandToParamsList(content);

                List<String> imageUrlList;
                List<Image> imageList = new ArrayList<>();
                ByteArrayOutputStream os = new ByteArrayOutputStream();

                //调用pixivApi
                try {
                    if (content.contains("R18") || content.contains("r18")) {
                        //是否包含 R18 TAG
                        imageUrlList = PixivApi.getR18ImageList(tags);
                    } else {
                        imageUrlList = PixivApi.getImageList(tags);
                    }
                    for (String imageUrl : imageUrlList) {
                        BufferedImage bufferedImage = ImgUtil.read(new URL(imageUrl));
                        ImageIO.write(bufferedImage, "PNG", os);
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(os.toByteArray());
                        Image image = Contact.uploadImage(groupMessageEvent.getSubject(), inputStream);
                        imageList.add(image);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //发送返回消息
                Group subject = groupMessageEvent.getSubject();
                MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
                for (Image image : imageList) {
                    messageChainBuilder.append(image);
                }

                subject.sendMessage(messageChainBuilder.build());
            }
        }
    }

    @Override
    public boolean isSelect(String module) {
        return CommandConfig.PIXIV.equalsIgnoreCase(module);
    }
}
