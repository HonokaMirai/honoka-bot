package com.honoka;

import com.honoka.config.*;
import com.honoka.handler.*;
import com.honoka.mirai.config.DatabaseMybatisConfig;
import com.honoka.mirai.config.OpenAiGptConfig;
import com.honoka.mirai.config.YoutubeApiConfig;
import io.github.mzdluo123.silk4j.AudioUtils;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

/**
 * @author honoka
 * @description 启动类
 * @date 2022-10-11 17:48:24
 */
public class HonokaBotApplication {

    public static void start(JavaPlugin plugin) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // 初始化日志对象
        BotConfig.logger = plugin.getLogger();
        // 初始化配置
        CommandConfig config = new CommandConfig();
        config.initCommandList(plugin);
        BotConfig.logger.info("初始化完成CommandConfig");
        // 初始化监听器
        GroupEventListener groupEventListener = new GroupEventListener();
        groupEventListener.initGroupMessageListener(plugin);
        BotConfig.logger.info("初始化完成GroupEventListener");

        // 初始化config
        reloadPluginConfig(plugin);
        // 初始化data
        registerCommand();

        // 初始化文件夹
        FileConfig.checkDirectory();
        BotConfig.logger.info("初始化文件夹成功");

        // 初始化工具类
        InitUtilConfig.init();
        BotConfig.logger.info("初始化工具类成功");

    }

    /**
     * 初始化config
     * @param plugin
     */
    private static void reloadPluginConfig(JavaPlugin plugin) {
        plugin.reloadPluginConfig(OpenAiGptConfig.INSTANCE);
        plugin.reloadPluginConfig(DatabaseMybatisConfig.INSTANCE);
        plugin.reloadPluginConfig(YoutubeApiConfig.INSTANCE);
    }

    /**
     * 注册指令
     */
    private static void registerCommand() {
        CommandManager.INSTANCE.registerCommand(HelpCommandHandler.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(VtuberAudioCommandHandler.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(ChatGPTCommandHandler.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(MusicAudioCommandHandler.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(StableDiffusionCommandHandler.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(UserSystemHandler.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(UmamusumeCommandHandler.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(RequestMusicCommandHandler.INSTANCE, true);
        CommandManager.INSTANCE.registerCommand(WaifuImageCommandHandler.INSTANCE, true);

        BotConfig.logger.info("注册指令成功");
    }
}
