package com.honoka.component;

import cn.hutool.core.util.StrUtil;
import com.honoka.model.ai.ChatEvent;
import com.honoka.model.ai.GroupContext;
import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.SequenceGroup;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.MessageChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : HonokaMirai
 * @version : 1.0
 * @since : 2025-02-15
 */
public class GroupAutoReplyManager {

    /**
     * 群组消息环缓存
     */
    public static final Map<Long, GroupContext> GROUP_HISTORY_MAP = new ConcurrentHashMap<>();

    /**
     * 支持的群组列表
     */
    public static List<Long> ENABLE_GROUP_LIST = new ArrayList<>();

    /**
     * 系统提示词
     */
    public static String SYSTEM_PROMPT = "";

    /**
     * 回答频率
     */
    public static Long COLLECT_COUNT = 15L;

    /**
     * 机器人的qq
     */
    public static Long MYSELF_QQ = 0L;

    public static GroupContext getOrCreateGroupContext(Long groupId, Group group) {
        return GROUP_HISTORY_MAP.computeIfAbsent(groupId, id -> {
            // 每个群组独立Disruptor实例
            Disruptor<ChatEvent> disruptor = new Disruptor<>(
                    ChatEvent::new,
                    64,
                    DaemonThreadFactory.INSTANCE,
                    ProducerType.MULTI,
                    new YieldingWaitStrategy()
            );

            // 群组专属处理器链
            GroupMessageHandler handler = new GroupMessageHandler();
            BatchEventProcessor<ChatEvent> processor =
                    new BatchEventProcessor<>(
                            disruptor.getRingBuffer(),
                            disruptor.getRingBuffer().newBarrier(),
                            handler
                    );

            // 将处理器绑定到Disruptor（重要）
            disruptor.handleEventsWith(processor);

            GroupContext context = new GroupContext();
            context.setRingBuffer(disruptor.start());
            SequenceGroup sequenceGroup = new SequenceGroup();
            sequenceGroup.add(processor.getSequence());
            context.setSequences(sequenceGroup);
            context.setGroup(group);
            return context;
        });
    }

    /**
     * 接受群组消息
     * @param sender
     * @param message
     */
    public static void onGroupMessage(Member sender, MessageChain message) {
        Group group = sender.getGroup();
        long groupId = group.getId();
        long qq = sender.getId();
        long currentTimeMillis = System.currentTimeMillis();
        // 判断是否需要处理
        if (!ENABLE_GROUP_LIST.contains(groupId)) {
            return;
        }
        if (qq == MYSELF_QQ) {
            return;
        }
        if (StrUtil.isBlank(message.contentToString())) {
            return;
        }
        // 开始进行处理
        GroupContext groupContext = getOrCreateGroupContext(groupId, group);
        long sequence = groupContext.getRingBuffer().next();
        try {
            // 创建新消息的实例
            ChatEvent event = groupContext.getRingBuffer().get(sequence);
            event.setGroupId(groupId);
            event.setQq(qq);
            event.setNickname(sender.getNick());
            event.setTimestamp(currentTimeMillis);
            event.setContent(message.contentToString());
        } finally {
            // 将新消息放置在环形缓冲区中
            groupContext.setLastActiveTime(currentTimeMillis);
            groupContext.getRingBuffer().publish(sequence);
        }
    }
}
