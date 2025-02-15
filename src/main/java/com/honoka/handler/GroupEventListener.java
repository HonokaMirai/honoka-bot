package com.honoka.handler;

import cn.hutool.core.util.StrUtil;
import com.honoka.HonokaBotPlugin;
import com.honoka.component.GroupAutoReplyManager;
import com.honoka.config.BotConfig;
import com.honoka.config.UrlMessageConfig;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 监听所有消息
 */
public class GroupEventListener {

    public static final EventChannel<Event> INSTANCE = GlobalEventChannel.INSTANCE.parentScope(HonokaBotPlugin.INSTANCE);

    /**
     * 初始化群聊消息监听器
     * @param plugin
     */
    public void initGroupMessageListener(JavaPlugin plugin) {
        // 监听群聊消息
        INSTANCE.subscribeAlways(GroupMessageEvent.class, g -> {
            // 处理不同特殊类型的消息
            this.handleDifferentMessage(g.getSender(), g.getMessage());
        });
    }

    public void initAssistantMessageListener(JavaPlugin plugin) {
        // 创建一个定时轮询任务
        // 组合触发条件：时间窗口(30s) + 消息数量(3-5条)
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // 启动定时检查（每10秒检查一次）
        scheduler.scheduleAtFixedRate(() -> {
            // 执行任务检查

        }, 0, 20, TimeUnit.SECONDS);
    }

    /**
     * 处理不同类型的消息
     * @param sender 发送者
     * @param message 消息链
     */
    private void handleDifferentMessage(Member sender, MessageChain message) {
        // 获取消息链中的第一个消息SingleMessage
        SingleMessage singleMessage = null;
        for (SingleMessage value : message) {
            if (StrUtil.isNotBlank(value.contentToString())) {
                singleMessage = value;
            }
        }

        if (singleMessage instanceof LightApp) {
            // 是一个小程序消息，识别出该小程序的信息
            LightAppMessageCommandHandler.parseLightAppMessage(sender, singleMessage);
            return;
        }
        if (singleMessage instanceof OnlineAudio) {
            // 是一个语音消息，调用Whisper服务
            ChatGPTCommandHandler.onlineAudioToWhisper(sender, message);
            return;
        }
        if (singleMessage instanceof FileMessage) {
            // 这是一个文件消息，判断类型然后再处理
            FileMessageCommandHandler.mp3FileToWhisper(sender, message);
            return;
        }
        if (message.contentToString().contains(UrlMessageConfig.Constant.HTTP)) {
            // 是包含一个URL地址消息，进行信息解析
            UrlMessageCommandHandler.parseUrlMessage(sender, message);
            return;
        }

        // 对话回复
        GroupAutoReplyManager.onGroupMessage(sender, message);
    }
}
