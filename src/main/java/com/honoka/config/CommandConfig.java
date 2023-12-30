package com.honoka.config;

import com.honoka.handler.CommandHandler;
import com.honoka.handler.impl.*;
import lombok.Data;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;

import java.util.*;

/**
 * @author honoka
 * @description 相关指令配置
 * @date 2022-10-12 10:42:57
 */
@Data
public class CommandConfig {

    public static final String COMMAND_PREFIX = "-";

    public static final String HELP = "HELP";
    /**
     * 模版：-ai text xxx,xxx,xxx,{{xxx]}
     *      -ai i2i xxx,xxx,xxx img_path
     *      1girl, simple background, nsfw, (pants), mess, (cum on body:1.1), wet, mist, stream, crying, medium breast, short curly messy hair, (blue hair:1.1) (pink medium hair:1.1), beautiful aqua gradient blue and pink eyes, (torn_clothes:1.157), nude, pink clothes, streaming tear, upper body
     */
    public static final String NOVEL_AI = "AI";

    public static final String PIXIV = "PIXIV";


    public static List<CommandHandler> COMMAND_LIST = new ArrayList<>(10);
    public static Map<String, CommandHandler> COMMAND_MAP = new HashMap<>(16);


    public static boolean isCommand(String message) {
        return message.startsWith(COMMAND_PREFIX);
    }

    public void initCommandList(JavaPlugin plugin) {

        PixivCommandHandler pixivCommandHandler = new PixivCommandHandler();

        COMMAND_LIST.add(pixivCommandHandler);

    }

    public static CommandHandler getCommandHandler(String module) {
        for (CommandHandler commandHandler : COMMAND_LIST) {
            if (commandHandler.isSelect(module)) {
                return commandHandler;
            }
        }
        BotConfig.logger.info("没有对应的handler：" + module);
        return null;
    }

    public static List<CommandHandler> getCommandList() {
        return COMMAND_LIST;
    }
}
