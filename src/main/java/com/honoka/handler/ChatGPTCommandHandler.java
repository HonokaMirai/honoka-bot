package com.honoka.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.honoka.HonokaBotPlugin;
import com.honoka.api.resp.ChatGptPromptResp;
import com.honoka.api.resp.GptEmotionResp;
import com.honoka.config.BotConfig;
import com.honoka.config.ChatGPTConfig;
import com.honoka.config.FileConfig;
import com.honoka.dao.entity.GptPromptConfig;
import com.honoka.dao.mapper.GptPromptConfigMapper;
import com.honoka.mirai.config.OpenAiGptConfig;
import com.honoka.model.ai.AssistantCompletionMessage;
import com.honoka.model.ai.UserChatMessage;
import com.honoka.util.CommandUtil;
import com.honoka.util.MiraiUtil;
import com.honoka.util.MybatisUtil;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.whisper.WhisperResponse;
import com.unfbx.chatgpt.exception.BaseException;
import io.github.mzdluo123.silk4j.AudioUtils;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author honoka
 * @description ChatGPT相关
 * @date 2023-02-02 01:28:07
 */
public class ChatGPTCommandHandler extends JRawCommand {

    public static final ChatGPTCommandHandler INSTANCE = new ChatGPTCommandHandler();

    public ChatGPTCommandHandler() {
        super(HonokaBotPlugin.INSTANCE, "c");
        setUsage("/c");
        setDescription("发送ChatGpt消息");
        setPrefixOptional(true);
    }

    /**
     * /c [prompt]
     * /c t sys [prompt]
     * /c t [prompt]
     * /c img [prompt]
     * /c end
     *
     * @param context
     * @param args
     */
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
        String operate = args.get(0).contentToString();

        // 获取用户会话列表
        List<Message> userMessageList = new ArrayList<>();

        switch (operate) {
            case ChatGPTConfig.Command.SET:
                // 设置所有人的单次会话预设
                this.setSystemPreset(args);
                return;
            case ChatGPTConfig.Command.MODEL:
                // 设置模型
                this.setModel(args);
                // 回复消息
                sender.getSubject().sendMessage("设置完成~");
                return;
            case ChatGPTConfig.Command.MODELS:
                // 获取模型列表
                sender.getSubject().sendMessage("可支持的模型列表: \n" + JSONObject.toJSONString(ChatGPTConfig.MODELS));
                return;
            case ChatGPTConfig.Command.CHARACTER:
                // 设置角色
                this.setSystemCharacter(args);
                return;
            case ChatGPTConfig.Command.REFRESH:
                // 清除所有人的单次会话预设
                ChatGPTConfig.refreshSystemPreset();
                // 刷新GPT配置
                ChatGPTConfig.refreshGPTConfig();
                return;
            default:
                // 执行一次性对话
                this.singleChat(args, userMessageList, context);
                return;
        }

    }

    /**
     * 装载用户的提问
     * @param messageChain
     * @return
     */
    public UserChatMessage setupChatMessage(MessageChain messageChain) {
        UserChatMessage userChatMessage = new UserChatMessage();
        int index = 0;
        // 1.装载请求使用的模型
        String firstParam = messageChain.get(0).contentToString();
        if (ChatGPTConfig.MODELS.contains(firstParam)) {
            // 第一个参数是模型
            userChatMessage.setModel(firstParam);
            // 拼接剩下的参数
            index = 1;
        } else {
            // 第一个参数就是提问，使用默认模型
            userChatMessage.setModel(ChatGPTConfig.DEFAULT_MODEL);
        }
        // 2.装载用户的提问
        StringBuilder sb = new StringBuilder();
        for (int i = index; i < messageChain.size(); i++) {
            sb.append(messageChain.get(i).contentToString()).append(" ");
        }
        userChatMessage.setMessage(sb.toString());
        // 3.装载上传的文件

        return userChatMessage;
    }

    /**
     * 发送带表情的情感消息
     * @param sender
     * @param text
     * @throws IOException
     */
    private void sendEmotionMessage(CommandSender sender, String text) throws IOException {
        //  解析预设
        GptEmotionResp emotionResp = JSONObject.parseObject(text, GptEmotionResp.class);
        String answer = emotionResp.getAnswer();
        String emotion = emotionResp.getEmotion();

        // 通过emotion查询对应的图片
        File emotionFile = FileConfig.getFileByEmotion(emotion);
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        if (Objects.nonNull(emotionFile)) {
            // 将文件转换为输入流
            InputStream inputStream = new FileInputStream(emotionFile);
            Image image = Contact.uploadImage(sender.getSubject(), inputStream);
            messageChainBuilder.append(image);
            inputStream.close();
        }
        // 构造回复消息
        MessageChain emotionMessage = messageChainBuilder.append(answer).build();
        sender.getSubject().sendMessage(emotionMessage);
    }

    /**
     * 设置系统预设
     * @param args
     */
    private void setSystemPreset(MessageChain args) {
        String prompt = args.get(1).contentToString();
        // 查询对应的预设
        GptPromptConfig config = MybatisUtil.execute(sqlSession ->
                sqlSession.getMapper(GptPromptConfigMapper.class).selectConfigByType(prompt));
        if (Objects.isNull(config)) {
            // 未查询到预设
            return;
        }
        ChatGPTConfig.setSystemPreset(config);
        BotConfig.logger.info("GPT预设已设置为: " + JSONObject.toJSONString(config));
    }

    /**
     * 设置模型
     * @param args
     */
    private void setModel(MessageChain args) {
        ChatGPTConfig.setModel(args.get(1).contentToString());
    }

    /**
     * 设置系统角色
     * @param args
     */
    private void setSystemCharacter(MessageChain args) {
        ChatGPTConfig.character = args.get(1).contentToString();
        BotConfig.logger.info("GPT角色已设置为: " + ChatGPTConfig.character);
    }

    /**
     * 设置持续性对话的system

     * @param args            消息链
     * @param userMessageList 用户会话列表
     */
    private void setSystemMessage(MessageChain args, List<Message> userMessageList) {
        StringBuilder sb = new StringBuilder();
        // 设置System消息
        for (int i = 1; i < args.size(); i++) {
            sb.append(args.get(i).contentToString()).append(" ");
        }
        userMessageList.add(0, Message.builder().role(Message.Role.SYSTEM.getName()).content(sb.toString()).build());
    }

    /**
     * 构建回复消息
     * @param args
     * @return
     */
    private String buildMessageContent(MessageChain args) {
        StringBuilder sb = new StringBuilder();
        // 构建消息
        for (int i = 1; i < args.size(); i++) {
            sb.append(args.get(i).contentToString()).append(" ");
        }
        return sb.toString();
    }

    /**
     * 发起一次性对话
     *
     * @param args            消息链
     * @param userMessageList 用户会话列表
     */
    private void singleChat(MessageChain args, List<Message> userMessageList, CommandContext context) {
        // 1.装载用户的提问
        UserChatMessage userChatMessage = this.setupChatMessage(args);
        if (StrUtil.isBlank(userChatMessage.getMessage())) {
            // 提问为空，直接返回
            return;
        }

        // 发起一次问答对话
        Message systemPreset = ChatGPTConfig.getSystemPresetPrompt();
        if (Objects.nonNull(systemPreset)) {
            userMessageList.add(systemPreset);
        }

        userMessageList.add(Message.builder()
                .role(Message.Role.USER.getName())
                .content(userChatMessage.getMessage())
                .build()
        );

        // 2.发起请求
        AssistantCompletionMessage assistantCompletionMessage = ChatGPTConfig.handleChatCompletionApi(userChatMessage.getModel(), userMessageList, context);

        // 3.回复消息
        context.getSender().getSubject().sendMessage(CommandUtil.filter(assistantCompletionMessage.getContent()));
    }

    /**
     * 通过Whisper语音转文字
     *
     * @param sender       消息发送人
     * @param messageChain 语音消息链
     */
    public static void onlineAudioToWhisper(User sender, MessageChain messageChain) {
        for (SingleMessage singleMessage : messageChain) {
            //判断是否为一个在线语音消息，即群员发送的消息
            if (singleMessage instanceof OnlineAudio) {
                OnlineAudio onlineAudio = (OnlineAudio) singleMessage;
                String urlForDownload = onlineAudio.getUrlForDownload();
                //BotConfig.logger.info("获取到语音下载链接: " + urlForDownload);
                File file = FileConfig.urlToFile(urlForDownload, "");
                //BotConfig.logger.info("QQ在线语音缓存地址: " + file.getAbsolutePath());
                File mp3File = convertSilkToMp3(file);
                String text = "";
                try {
                    WhisperResponse whisperResponse = ChatGPTConfig.getOpenAiClient().speechToTextTranscriptions(mp3File);
                    text = whisperResponse.getText();
                } catch (Exception e) {
                    BotConfig.logger.error(e);
                }
                //调用成功，发送消息
                MessageChain message = new MessageChainBuilder()
                        .append(new QuoteReply(messageChain))
                        .append(new At(sender.getId()))
                        .append("   ")
                        .append(text).build();
                Group group = ((Member) sender).getGroup();
                group.sendMessage(message);
            }

        }
    }

    /**
     * 将一个QQ语音的SILK文件转换成MAP3格式的语音文件
     *
     * @param file silk文件
     * @return file MP3文件
     */
    public static File convertSilkToMp3(File file) {
        File mp3File = null;
        try {
            mp3File = AudioUtils.silkToMp3(file);
        } catch (IOException e) {
            BotConfig.logger.error("语音格式转换失败: " + e);
        }
        return mp3File;
    }

    /**
     * 发送一条自然语言处理成PROMPT的消息
     */
    public static ChatGptPromptResp handleStableDiffusionTemplateContent(String content) {
        //获取配置类信息
        String sysPrompt = OpenAiGptConfig.INSTANCE.SD_PROMPT.get();

        List<Message> userMessageList = new ArrayList<>();
        Message systemMsg = Message.builder().role(Message.Role.SYSTEM.getName()).content(sysPrompt).build();
        Message userMsg = Message.builder().role(Message.Role.USER.getName()).content(content).build();

        userMessageList.add(systemMsg);
        userMessageList.add(userMsg);

        //开始调用GPT3.5API
        ChatCompletionResponse chatResponse = ChatGPTConfig.getOpenAiClient().chatCompletion(ChatGPTConfig.getChatCompletion(ChatGPTConfig.DEFAULT_MODEL, userMessageList));
        List<ChatChoice> choices = chatResponse.getChoices();
        for (ChatChoice choice : choices) {
            String text = choice.getMessage().getContent();
            if (StringUtils.isNotBlank(text)) {
                //将回答的信息转换成json格式
                BotConfig.logger.info("请求给GPT的消息为:" + JSONObject.toJSONString(userMessageList));
                BotConfig.logger.info("获取到GPT返回的回答:" + text);
                try {
                    ChatGptPromptResp chatGptPromptVO = JSONObject.parseObject(text, ChatGptPromptResp.class);
                    return chatGptPromptVO;
                } catch (Exception e) {
                    BotConfig.logger.error("获取到GPT返回的回答时发生错误:" + e);
                    return null;
                }
            }
        }
        return null;
    }

}
