package com.honoka.util;

import com.honoka.HonokaBotPlugin;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.io.IOException;

/**
 * @description Mirai工具类
 * @author honoka
 * @date 2022-10-11 17:48:24
 */
public class MiraiUtil {

    /**
     * 类加载器切换
     * @param block
     */
    public static void changeClassLoader(Runnable block) {
        Thread thread = Thread.currentThread();
        ClassLoader oc = thread.getContextClassLoader();
        try {
            thread.setContextClassLoader(HonokaBotPlugin.class.getClassLoader());
            block.run();
        } finally {
            thread.setContextClassLoader(oc);
        }
    }

    /**
     * 构建@回复消息
     *
     * @param quoteReply @引用消息
     * @param qq         用户qq
     * @param content    回复内容
     * @return
     */
    public static MessageChain buildQuoteReplyMessage(MessageChain quoteReply, Long qq, String content) {
        return new MessageChainBuilder()
                .append(new QuoteReply(quoteReply))
                .append(new At(qq))
                .append("    ")
                .append(content)
                .build();
    }

    /**
     * 构建@ + 音频回复消息
     * @param context
     * @param file
     * @return
     */
    public static Audio buildAudioMessage(CommandContext context, File file) {

        Group group = (Group) context.getSender().getSubject();
        ExternalResource resource = null;
        Audio audio = null;
        try {
            // 上传文件得到语音实例
            resource = ExternalResource.create(file);
            audio = group.uploadAudio(resource);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 保证资源正常关闭
            try {
                resource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return audio;
    }

}
