package com.honoka.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honoka.HonokaBotPlugin;
import com.honoka.config.UmamusumeConfig;
import com.honoka.config.UserSystemConfig;
import com.honoka.dao.entity.User;
import com.honoka.dao.mapper.UserMapper;
import com.honoka.util.MiraiUtil;
import com.honoka.util.MybatisUtil;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @description 用户系统相关指令
 * @author honoka
 * @date 2023-07-16 00:38:48
 */
public class UserSystemHandler extends JRawCommand {
    public static final UserSystemHandler INSTANCE = new UserSystemHandler();

    public UserSystemHandler() {
        super(HonokaBotPlugin.INSTANCE, "sys");
        setUsage("/sys");
        setDescription("用户系统操作");
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
            case UserSystemConfig.Command.REGISTER:
                // 用户注册
                reply = this.register(qq, sender.getUser().getNick());
                break;
            case UserSystemConfig.Command.REFRESH_UMA:
                // 刷新马娘卡池
                reply = this.refreshUmaGachaPool();
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
     * 用户注册
     * @param qq
     * @param nick
     * @return
     */
    private String register(Long qq, String nick){
        // 查询用户是否存在
        User exist = MybatisUtil.execute(sqlSession ->
                sqlSession.getMapper(UserMapper.class).selectOne(new QueryWrapper<User>()
                        .eq(User.QQ, qq))
        );
        if (Objects.nonNull(exist)) {
            // 用户已存在
            return UserSystemConfig.Reply.USER_EXIST;
        }
        // 用户不存在，创建用户
        User user = new User();
        user.setQq(qq);
        user.setNick(nick);
        user.setDiamond(UserSystemConfig.DIAMOND);

        MybatisUtil.execute(sqlSession ->
                sqlSession.getMapper(UserMapper.class).insert(user)
        );
        return UserSystemConfig.Reply.REGISTER_SUCCESS;
    }

    /**
     * 刷新马娘卡池
     * @return
     */
    private String refreshUmaGachaPool() {
        UmamusumeConfig.initGachaPool();
        return UserSystemConfig.Reply.REFRESH_SUCCESS;
    }
}
