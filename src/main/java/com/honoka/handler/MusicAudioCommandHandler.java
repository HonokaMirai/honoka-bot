package com.honoka.handler;

import com.honoka.HonokaBotPlugin;
import com.honoka.config.FileConfig;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * @description 音乐相关
 * @author honoka
 * @date 2023-03-20 22:17:53
 */
public class MusicAudioCommandHandler extends JRawCommand {

    public static final MusicAudioCommandHandler INSTANCE = new MusicAudioCommandHandler();

    public MusicAudioCommandHandler() {
        super(HonokaBotPlugin.INSTANCE, "m");
        setUsage("/m");
        setDescription("音乐音频发送指令");
        setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        //为群员发送消息时，转换成Group对象
        Group group = (Group)sender.getSubject();
        //随机从文件夹下获取一个文件
        File randomFile = FileConfig.getRandomFile(FileConfig.MUSIC_PATH);

        ExternalResource resource = null;
        Audio audio = null;
        try {
            // 上传文件得到语音实例
            resource = ExternalResource.create(randomFile);
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
        String prefixName = randomFile.getName().split("\\.")[0];
        //返回消息给群
        group.sendMessage("正在播放: " + prefixName);
        group.sendMessage(audio);

    }

}
