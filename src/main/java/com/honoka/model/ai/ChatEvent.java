package com.honoka.model.ai;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HonokaMirai
 * @version : 1.0
 * @since : 2025-02-15
 */
@Data
@Accessors(chain = true)
public class ChatEvent {

    // 消息所属群组
    private Long groupId;

    // 消息发送人qq
    private Long qq;

    // 消息发送人昵称
    private String nickname;

    // 消息发送时间
    private Long timestamp;

    // 消息内容
    private String content;

}
