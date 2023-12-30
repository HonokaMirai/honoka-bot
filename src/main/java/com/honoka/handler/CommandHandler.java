package com.honoka.handler;

import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * @author honoka
 * @description 公共命令处理接口
 * @date 2022-10-12 11:54:31
 */
public interface CommandHandler {

    void handle(GroupMessageEvent groupMessageEvent, String command);

    boolean isSelect(String module);
}
