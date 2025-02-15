package com.honoka.component;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.honoka.config.BotConfig;
import com.honoka.config.ChatGPTConfig;
import com.honoka.model.ai.AssistantCompletionMessage;
import com.honoka.model.ai.ChatEvent;
import com.honoka.model.ai.GroupContext;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.unfbx.chatgpt.entity.chat.BaseMessage;
import com.unfbx.chatgpt.entity.chat.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : HonokaMirai
 * @version : 1.0
 * @since : 2025-02-15
 */

public class GroupMessageHandler implements EventHandler<ChatEvent> {

    @Override
    public void onEvent(ChatEvent chatEvent, long sequence, boolean endOfBatch) {
        GroupContext groupContext = GroupAutoReplyManager.getOrCreateGroupContext(chatEvent.getGroupId(), null);
        long lastSequence = groupContext.getLastSequence();
        long sequenceCount = sequence - lastSequence;
        long collectCount = 15;
        if (sequenceCount < collectCount) {
            // 不需要处理
            return;
        }
        // 需要处理
        List<ChatEvent> eventList = new ArrayList<>();
        // 设置最新消息序号
        groupContext.setLastSequence(sequence);
        // 取出最近的15条消息
        RingBuffer<ChatEvent> ringBuffer = groupContext.getRingBuffer();
        for (long i = collectCount; i > 0; i--) {
            ChatEvent event = ringBuffer.get(sequence - i + 1);
            eventList.add(event);
        }
        // 处理请求
        String content = this.handleChatEventList(eventList);
        // 发送消息
        groupContext.getGroup().sendMessage(content);
    }

    public String handleChatEventList(List<ChatEvent> eventList) {
        List<Message> messageList = new ArrayList<>();
        messageList.add(Message.builder().role(BaseMessage.Role.SYSTEM).content(GroupAutoReplyManager.SYSTEM_PROMPT).build());
        // 将消息列表转换成GPT需要的格式
        StringBuilder sb = new StringBuilder();
        String template = "{}({}): {}";
        for (ChatEvent event : eventList) {
            String format = StrUtil.format(template, event.getNickname(), event.getQq(), event.getContent()) + "\n";
            sb.append(format);
        }
        messageList.add(Message.builder().role(BaseMessage.Role.USER).content(sb.toString()).build());
        // 发送API请求，处理返回接口
        BotConfig.logger.info("开始处理聊天事件列表: " + JSONObject.toJSONString(messageList));
        AssistantCompletionMessage assistantCompletionMessage = ChatGPTConfig.handleChatCompletionApi(messageList);
        return assistantCompletionMessage.getContent();
    }

}
