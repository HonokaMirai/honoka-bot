package com.honoka.handler;

import cn.hutool.core.util.StrUtil;
import com.honoka.HonokaBotPlugin;
import com.honoka.util.MiraiUtil;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author : HonokaMirai
 * @version : 1.0
 * @since : 2024-05-29
 */
public class SchoolImasCommandHandler extends JRawCommand {

    public static final SchoolImasCommandHandler INSTANCE = new SchoolImasCommandHandler();

    public SchoolImasCommandHandler() {
        super(HonokaBotPlugin.INSTANCE, "imas");
        setUsage("/imas");
        setDescription("偶像大师");
        setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandContext context, @NotNull MessageChain args) {
        CommandSender sender = context.getSender();
        // 获取发送者的QQ
        long qq = sender.getUser().getId();

        // 获取请求参数
        int vo = Integer.parseInt(args.get(0).contentToString());
        int da = Integer.parseInt(args.get(1).contentToString());
        int vi = Integer.parseInt(args.get(2).contentToString());

        // 结算分数
        vo = Math.min(vo + 30, 1500);
        da = Math.min(da + 30, 1500);
        vi = Math.min(vi + 30, 1500);

        // 计算所需PT分数
        // 属性总分
        int totalScore = vo + da + vi;
        // 角色属性评分
        int attributeScore = new BigDecimal("2.3").multiply(new BigDecimal(totalScore)).setScale(0, RoundingMode.DOWN).intValue();

        // 第一名评分
        final int firstRankScore = 1700;
        // 最终试验PT评分
        final int aPlusTargetScore = 11500;
        final int sTargetScore = 13000;

        // 计算所需PT分数
        int aPlusRequiredPoints = aPlusTargetScore - firstRankScore - attributeScore;
        int sRequiredPoints = sTargetScore - firstRankScore - attributeScore;

        int aPlusResultPoints = calculateMinimumPT(aPlusRequiredPoints);
        int sResultPoints = calculateMinimumPT(sRequiredPoints);

        // 返回消息
        StringBuilder sb = new StringBuilder();
        sb.append("\nA+需要的PT分数为: ")
                .append(aPlusResultPoints)
                .append("\n")
                .append("S需要的PT分数为: ")
                .append(sResultPoints)
                .append("\n")
                .append("结束后属性为Vo: ")
                .append(vo)
                .append(" Da: ")
                .append(da)
                .append(" Vi: ")
                .append(vi)
                .append("\n")
                .append("结束后总属性为: ")
                .append(totalScore)
        ;
        MessageChain message = MiraiUtil.buildQuoteReplyMessage(context.getOriginalMessage(), qq, sb.toString());
        sender.getSubject().sendMessage(message);

    }

    private static int calculateMinimumPT(int requiredPoints) {
        if (requiredPoints< 0) {
            return 0;
        }
        int pt;

        if (requiredPoints <= 5000 * 0.3) {
            // 5000 * 0.3 = 1500
            pt = (int) Math.ceil(requiredPoints / 0.3);
        } else if (requiredPoints <= 5000 * 0.3 + 5000 * 0.15) {
            // 5000 * 0.3 + 5000 * 0.15 = 2250
            pt = (int) Math.ceil((requiredPoints - 5000 * 0.3) / 0.15 + 5000);
        } else if (requiredPoints <= 5000 * 0.3 + 5000 * 0.15 + 10000 * 0.08) {
            // 5000 * 0.3 + 5000 * 0.15 + 10000 * 0.08 = 3050
            pt = (int) Math.ceil((requiredPoints - 5000 * 0.3 - 5000 * 0.15) / 0.08 + 10000);
        } else if (requiredPoints <= 5000 * 0.3 + 5000 * 0.15 + 10000 * 0.08 + 10000 * 0.04) {
            // 5000 * 0.3 + 5000 * 0.15 + 10000 * 0.08 + 10000 * 0.04 = 4050
            pt = (int) Math.ceil((requiredPoints - 5000 * 0.3 - 5000 * 0.15 - 10000 * 0.08) / 0.04 + 20000);
        } else if (requiredPoints <= 5000 * 0.3 + 5000 * 0.15 + 10000 * 0.08 + 10000 * 0.04 + 10000 * 0.02) {
            // 5000 * 0.3 + 5000 * 0.15 + 10000 * 0.08 + 10000 * 0.04 + 10000 * 0.02 = 5050
            pt = (int) Math.ceil((requiredPoints - 5000 * 0.3 - 5000 * 0.15 - 10000 * 0.08 - 10000 * 0.04) / 0.02 + 30000);
        } else if (requiredPoints <= 5000 * 0.3 + 5000 * 0.15 + 10000 * 0.08 + 10000 * 0.04 + 10000 * 0.02 + 10000 * 0.01) {
            // 5000 * 0.3 + 5000 * 0.15 + 10000 * 0.08 + 10000 * 0.04 + 10000 * 0.02 + 10000 * 0.01 = 6050
            pt = (int) Math.ceil((requiredPoints - 5000 * 0.3 - 5000 * 0.15 - 10000 * 0.08 - 10000 * 0.04 - 10000 * 0.02) / 0.01 + 40000);
        } else {
            pt = -1;
        }

        return pt;
    }

}
