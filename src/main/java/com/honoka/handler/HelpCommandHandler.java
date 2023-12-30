package com.honoka.handler;

import com.honoka.HonokaBotPlugin;
import com.honoka.config.HelpInfoConfig;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;

/**
 * @description 帮助命令处理类
 * @author honoka
 * @date 2023-03-20 22:24:09
 */
public class HelpCommandHandler extends JRawCommand {

    public static final HelpCommandHandler INSTANCE = new HelpCommandHandler();

    public HelpCommandHandler() {
        super(HonokaBotPlugin.INSTANCE, "h");
        setUsage("/h");
        setDescription("帮助指令");
        setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        sender.getSubject().sendMessage(HelpInfoConfig.getHelpInfo());
    }
}
