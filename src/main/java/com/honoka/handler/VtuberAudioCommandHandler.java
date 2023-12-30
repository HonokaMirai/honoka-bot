package com.honoka.handler;

import com.honoka.HonokaBotPlugin;
import com.honoka.config.BotConfig;
import com.honoka.config.FileConfig;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * @description 用于发送语音
 * @author Honoka
 * @date 2023-03-16 22:22:07
 */
public class VtuberAudioCommandHandler extends JRawCommand {

    public static final VtuberAudioCommandHandler INSTANCE = new VtuberAudioCommandHandler();

    public VtuberAudioCommandHandler() {
        super(HonokaBotPlugin.INSTANCE, "a");
        setUsage("/a");
        setDescription("Vtuber语音发送指令");
        setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {

        //为群员发送消息时，转换成Group对象
        Group group = (Group)sender.getSubject();
        File file = null;
        if (args.size() > 0) {
            //获取第一个参数
            String prompt = args.get(0).contentToString();
            if ("OP".equalsIgnoreCase(prompt) || "原批".equals(prompt)) {
                //获取嘉然OP语音
                file = FileConfig.getFileByPath(FileConfig.VTUBER_PATH + "/嘉然-我超，OP.silk");

            }

        } else {
            //随机从文件夹下获取一个文件
            file = FileConfig.getRandomFile(FileConfig.VTUBER_PATH);
        }

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

        //返回消息给群
        group.sendMessage(audio);
    }
}
