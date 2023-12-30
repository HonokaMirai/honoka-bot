package com.honoka.handler;

import cn.hutool.core.util.StrUtil;
import com.honoka.HonokaBotPlugin;
import com.honoka.api.SzfxApi;
import com.honoka.api.resp.NetEaseMusicSzfxEntity;
import com.honoka.config.BotConfig;
import com.honoka.config.FileConfig;
import com.honoka.util.MiraiUtil;
import io.github.mzdluo123.silk4j.AudioUtils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class RequestMusicCommandHandler extends JRawCommand {
    public static final RequestMusicCommandHandler INSTANCE = new RequestMusicCommandHandler();

    public RequestMusicCommandHandler() {
        super(HonokaBotPlugin.INSTANCE, "点歌");
        setUsage("/点歌");
        setDescription("点歌指令");
        setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandContext context, @NotNull MessageChain args) {
        // 拼接歌曲名
        StringBuilder songName = new StringBuilder();
        for (SingleMessage singleMessage : args) {
            songName.append(singleMessage.contentToString());
        }
        if (StrUtil.isNotBlank(songName.toString())) {
            // 开始进行点歌
            NetEaseMusicSzfxEntity entity = SzfxApi.getNetEaseMusic(songName.toString());
            if (Objects.nonNull(entity)) {
                // 开始解析歌曲
                File mp3File = FileConfig.urlToFile(entity.getUrl(), ".mp3");
                File silkFile = null;
                try {
                    silkFile = AudioUtils.mp3ToSilk(mp3File);
                } catch (IOException e) {
                    BotConfig.logger.error("mp3格式转换silk格式错误", e);
                }
                Audio audio = MiraiUtil.buildAudioMessage(context, silkFile);

                //返回消息给群
                Group group = (Group) context.getSender().getSubject();
                group.sendMessage("正在播放: " + Optional.ofNullable(entity.getSinger()).orElse("")
                        + " - " + Optional.ofNullable(entity.getSong()).orElse(""));
                group.sendMessage(audio);
            }

        }
    }
}
