package com.honoka.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honoka.HonokaBotPlugin;
import com.honoka.config.BotConfig;
import com.honoka.config.UmamusumeConfig;
import com.honoka.config.UserSystemConfig;
import com.honoka.dao.dto.UserUmaPackage;
import com.honoka.dao.entity.UmaGachaPool;
import com.honoka.dao.entity.UmaUserGachaLog;
import com.honoka.dao.entity.User;
import com.honoka.dao.mapper.UmaGachaPoolMapper;
import com.honoka.dao.mapper.UmaUserBackpackMapper;
import com.honoka.dao.mapper.UmaUserGachaLogMapper;
import com.honoka.dao.mapper.UserMapper;
import com.honoka.util.MiraiUtil;
import com.honoka.util.MybatisUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class UmamusumeCommandHandler extends JRawCommand {

    public static final UmamusumeCommandHandler INSTANCE = new UmamusumeCommandHandler();

    public UmamusumeCommandHandler() {
        super(HonokaBotPlugin.INSTANCE, "uma");
        setUsage("/uma");
        setDescription("发送马娘消息");
        setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandContext context, @NotNull MessageChain args) {
        CommandSender sender = context.getSender();
        if (CollectionUtil.isEmpty(args) || sender.getSubject() == null || sender.getUser() == null) {
            // 相关信息为空
            return;
        }
        // 获取发送者的QQ
        long qq = sender.getUser().getId();
        // 获取第一个参数
        String prompt = args.get(0).contentToString();

        String reply;
        switch (prompt){
            case UmamusumeConfig.Command.REFRESH:
                // 刷新卡池
                UmamusumeConfig.initGachaPool();
                return;
            case UmamusumeConfig.Command.GACHA:
                // 抽卡
                Integer times = this.checkTimes(args);
                if (Objects.isNull(times)) {
                    // 抽卡次数错误
                    sender.getSubject().sendMessage(UmamusumeConfig.Reply.TIMES_ERROR);
                    return;
                }
                reply = this.gacha(qq, times);
                break;
            case UmamusumeConfig.Command.TRAIN:
                // 训练
                reply = "";
                break;
            default:
                reply = UserSystemConfig.Reply.UNKNOWN;
                break;
        }

        // 返回结果
        if (StrUtil.isNotBlank(reply)) {
            MessageChain message = MiraiUtil.buildQuoteReplyMessage(context.getOriginalMessage(), qq, reply);
            sender.getSubject().sendMessage(message);
        }

    }

    /**
     * 抽卡
     * TODO: 添加事务
     * @param qq 用户QQ
     * @param times 抽卡次数
     * @return 抽卡结果
     */
    private String gacha(Long qq, Integer times) {
        // 判断用户是否存在
        User exist = MybatisUtil.execute(sqlSession ->
                sqlSession.getMapper(UserMapper.class).selectOne(new QueryWrapper<User>().eq("qq", qq))
        );
        if (Objects.isNull(exist)) {
            // 用户不存在
            return UserSystemConfig.Reply.USER_NOT_EXIST;
        }
        // 扣除用户钻石
        try {
            int update = MybatisUtil.execute(sqlSession ->
                    sqlSession.getMapper(UserMapper.class).updateUserDiamond(qq, UmamusumeConfig.Type.REDUCE, UmamusumeConfig.price * times)
            );
            if (update < 1) {
                // 扣除失败
                return UserSystemConfig.Reply.DIAMOND_NOT_ENOUGH + "，当前钻石数量为: " + exist.getDiamond();
            }
        } catch (Exception e) {
            BotConfig.logger.error("扣除用户钻石错误", e);
            return UserSystemConfig.Reply.DIAMOND_NOT_ENOUGH + "，当前钻石数量为: " + exist.getDiamond();
        }


        // 抽取卡池
        List<UmaGachaPool> gachaResultList = UmamusumeConfig.getRandomGachaList(times);

        // 记录用户抽卡日志
        List<UmaUserGachaLog> umaUserGachaLogList = new ArrayList<>(10);
        // 添加用户背包
        List<UserUmaPackage> gachaList = new ArrayList<>(10);

        for (UmaGachaPool umaGachaPool : gachaResultList) {
            umaUserGachaLogList.add(new UmaUserGachaLog()
                    .setUserId(exist.getId())
                    .setQq(qq)
                    .setUmaId(umaGachaPool.getUmaId())
                    .setUmaCnName(umaGachaPool.getUmaCnName())
            );
            gachaList.add(new UserUmaPackage()
                    .setUserId(exist.getId())
                    .setQq(qq)
                    .setUmaId(umaGachaPool.getUmaId())
                    .setUmaCnName(umaGachaPool.getUmaCnName())
                    .setNum(1)
            );
        }
        MybatisUtil.execute(sqlSession ->
                sqlSession.getMapper(UmaUserGachaLogMapper.class).insertBatchSomeColumnSelective(umaUserGachaLogList)
        );

        MybatisUtil.execute(sqlSession ->
                sqlSession.getMapper(UmaUserBackpackMapper.class).batchInsertOrUpdateGachaList(gachaList)
        );

        // 拼接返回结果
        StringBuilder reply = new StringBuilder();
        reply.append("本次抽取结果为: \n");
        for (UmaGachaPool umaGachaPool : gachaResultList) {
            reply.append(umaGachaPool.getUmaRare()).append(" : ").append(umaGachaPool.getUmaCnName()).append("\n");
        }

        return reply.toString();
    }

    /**
     * 校验抽卡次数是否合法
     * @param args
     * @return
     */
    private Integer checkTimes(MessageChain args) {
        Integer times = 1;
        try {
            times = Integer.valueOf(args.get(1).contentToString());
            // 判断times是否在1-10之间
            if (times < 1 || times > 10) {
                return null;
            }

        } catch (Exception e) {
            // 格式错误
            return null;
        }
        return times;
    }

}
