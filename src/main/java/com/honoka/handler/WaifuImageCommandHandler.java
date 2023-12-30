package com.honoka.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.honoka.HonokaBotPlugin;
import com.honoka.api.WaifuApi;
import com.honoka.config.BotConfig;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WaifuImageCommandHandler extends JRawCommand {

    public static final WaifuImageCommandHandler INSTANCE = new WaifuImageCommandHandler();

    public WaifuImageCommandHandler() {
        super(HonokaBotPlugin.INSTANCE, "来张色图");
        setUsage("/来张色图");
        setDescription("来张色图指令");
        setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandContext context, @NotNull MessageChain args) {
        Group group = (Group) context.getSender().getSubject();
        // 调用waifu-api
        String imageUrl = WaifuApi.getImage();
        // 上传图片
        MessageChainBuilder builder = new MessageChainBuilder();
        InputStream inputStream = null;
        try {
            if (StrUtil.isNotBlank(imageUrl)) {
                inputStream = new URL(URLUtil.normalize(imageUrl)).openConnection().getInputStream();
                Image image = Contact.uploadImage(group, inputStream);
                builder.append(image);
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
                .append("\n")
                .append(imageUrl)
                .build();
        group.sendMessage(sendMessage);
    }
}
