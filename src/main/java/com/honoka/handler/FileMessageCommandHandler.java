package com.honoka.handler;

import com.honoka.config.BotConfig;
import com.honoka.config.ChatGPTConfig;
import com.honoka.config.FileConfig;
import com.unfbx.chatgpt.entity.whisper.WhisperResponse;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.contact.file.AbsoluteFile;
import net.mamoe.mirai.message.data.*;

import java.io.File;

/**
 * @description 处理QQ文件消息
 * @author honoka
 * @date 2023-03-19 23:05:18
 */
public class FileMessageCommandHandler {

    public static void mp3FileToWhisper(User sender, MessageChain messageChain) {
        //先判断该文件是否为MP3格式
        for (SingleMessage singleMessage : messageChain) {
            if (singleMessage instanceof FileMessage) {
                FileMessage fileMessage = (FileMessage) singleMessage;
                String name = fileMessage.getName();
                //用正则判断是否为.mp3格式
                String regex1 = ".*\\.MP3$";
                String regex2 = ".*\\.mp3$";
                if (name.matches(regex1) || name.matches(regex2)) {
                    //下载该附件
                    AbsoluteFile absoluteFile = fileMessage.toAbsoluteFile(((Member) sender).getGroup());
                    //判断文件大小，要求小于25M
                    if (absoluteFile != null && absoluteFile.getSize() < 25 * 1024 * 1024) {
                        //获取文件下载链接
                        String downloadUrl = absoluteFile.getUrl();
                        BotConfig.logger.info("群文件的下载链接为: " + downloadUrl);
                        File mp3File = FileConfig.urlToFile(downloadUrl, ".mp3");
                        //调用Whisper服务
                        String text = "";
                        try {
                            WhisperResponse whisperResponse = ChatGPTConfig.getOpenAiClient().speechToTextTranscriptions(mp3File);
                            text = whisperResponse.getText();
                        }catch (Exception e) {
                            BotConfig.logger.error(e);
                        }
                        //调用成功，发送消息
                        MessageChain message = new MessageChainBuilder()
                                .append(new QuoteReply(messageChain))
                                .append(new At(sender.getId()))
                                .append("   ")
                                .append(text).build();
                        Group group = ((Member) sender).getGroup();
                        group.sendMessage(message);

                        //TODO: 删除缓存文件


                    }
                }
            }

        }
    }
}
