package com.honoka.config;

import lombok.Data;
import net.mamoe.mirai.console.logging.LoggerController;
import net.mamoe.mirai.utils.MiraiLogger;

/**
 * @description 机器人基础信息配置
 * @author honoka
 * @date 2022-10-12 15:46:05
 */
@Data
public class BotConfig {

    public volatile static MiraiLogger logger;

}
