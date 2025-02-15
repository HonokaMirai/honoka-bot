package com.honoka.model.ai;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceGroup;
import lombok.Data;
import net.mamoe.mirai.contact.Group;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author : HonokaMirai
 * @version : 1.0
 * @since : 2025-02-15
 */
@Data
public class GroupContext {

    // 群组消息环
    private RingBuffer<ChatEvent> ringBuffer;

    // 群对象
    private Group group;

    // 群组消息处理器
    private SequenceGroup sequences;

    // 群组最后活跃时间
    private long lastActiveTime;

    // 最新消息序号
    private long lastSequence;

}
