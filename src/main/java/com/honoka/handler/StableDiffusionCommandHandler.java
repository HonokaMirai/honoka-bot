package com.honoka.handler;

import cn.hutool.core.img.ImgUtil;
import com.alibaba.fastjson.JSONObject;
import com.honoka.HonokaBotPlugin;
import com.honoka.api.StableDiffusionApi;
import com.honoka.api.req.StableDiffusionApiConfigReq;
import com.honoka.api.req.StableDiffusionApiReq;
import com.honoka.api.resp.ChatGptPromptResp;
import com.honoka.api.resp.StableDiffusionApiResp;
import com.honoka.config.BotConfig;
import com.honoka.config.StableDiffusionConfig;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.SingleMessage;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class StableDiffusionCommandHandler extends JRawCommand {

    public static final StableDiffusionCommandHandler INSTANCE = new StableDiffusionCommandHandler();

    public StableDiffusionCommandHandler() {
        super(HonokaBotPlugin.INSTANCE, "sd");
        setUsage("/sd");
        setDescription("AI图片生成");
        setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        //为群员发送消息时，转换成Group对象
        Group group = (Group)sender.getSubject();

        StringBuilder sb = new StringBuilder();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = null;

        if (args.size() > 0) {
            //判断指令
            String arg1 = args.get(0).contentToString();
            if ("api".equals(arg1)) {
                //拼接剩下的参数
                for (int i = 1; i < args.size(); i++) {
                    sb.append(args.get(i).contentToString()).append(" ");
                }
                //请参数转换成API请求对象
                StableDiffusionApiReq apiRequest = JSONObject.parseObject(sb.toString(), StableDiffusionApiReq.class);
                //调用API请求
                try {
                    StableDiffusionApiResp stableDiffusionApiVO = StableDiffusionApi.postTxt2Img(apiRequest);
                    BufferedImage bufferedImage = ImgUtil.toImage(stableDiffusionApiVO.getImages());
                    ImageIO.write(bufferedImage, "PNG", os);
                } catch (Exception e) {
                    BotConfig.logger.error(e);
                    //发送异常消息
                    group.sendMessage("相关服务尚未启动哦~");
                } finally {
                    //关闭流
                    try {
                        os.close();
                    } catch (Exception e) {
                        BotConfig.logger.error(e);
                    }
                }

            } else if (StableDiffusionConfig.MODEL_LIST_COMMAND.equalsIgnoreCase(arg1)) {
                //调用API获取模型列表
                List<String> modelList = StableDiffusionApi.getSdModelList();
                StringBuilder models = new StringBuilder();
                modelList.forEach(s -> models.append(s).append("\n"));
                group.sendMessage(models.toString());
                return;
            } else if (StableDiffusionConfig.MODEL_COMMAND.equalsIgnoreCase(arg1)) {
                //请参数转换成API请求对象
                StableDiffusionApiConfigReq apiRequest = new StableDiffusionApiConfigReq();
                String arg2 = args.get(1).contentToString();
                apiRequest.setSdModelCheckpoint(arg2);
                StableDiffusionApi.setConfigOptions(apiRequest);
                group.sendMessage("设置完成");
                return;
            } else if (StableDiffusionConfig.CONFIG_COMMAND.equalsIgnoreCase(arg1)) {
                //拼接剩下的参数
                for (int i = 1; i < args.size(); i++) {
                    sb.append(args.get(i).contentToString()).append(" ");
                }
                //请参数转换成API请求对象
                StableDiffusionApiConfigReq apiRequest = JSONObject.parseObject(sb.toString(), StableDiffusionApiConfigReq.class);
                StableDiffusionApi.setConfigOptions(apiRequest);
                group.sendMessage("设置完成");
                return;
            } else if (StableDiffusionConfig.REFRESH_COMMAND.equalsIgnoreCase(arg1)) {
                //刷新model-list
                StableDiffusionApi.refreshCheckpoints();
                group.sendMessage("刷新model-list完成");
                return;
            } else {
                //无指令，拼接剩下的参数
                for (SingleMessage arg : args) {
                    sb.append(arg.contentToString()).append(" ");;
                }
                //开始调用GPT-API
                ChatGptPromptResp chatGptPromptVO = ChatGPTCommandHandler.handleStableDiffusionTemplateContent(sb.toString());
                if (chatGptPromptVO != null && chatGptPromptVO.getCode() == 0) {
                    String model = chatGptPromptVO.getModel();
                    String prompt = chatGptPromptVO.getPrompt();
                    BotConfig.logger.info("prompt为:" + prompt);
                    try {
                        //开始调用SD的API
                        String imageBase64Str = StableDiffusionApi.postTxt2ImgByGpt(chatGptPromptVO);
                        BufferedImage bufferedImage = ImgUtil.toImage(imageBase64Str);
                        ImageIO.write(bufferedImage, "PNG", os);
                    } catch (Exception e) {
                        BotConfig.logger.error(e);
                        //发送异常消息
                        group.sendMessage("相关服务尚未启动哦~");
                    } finally {
                        //关闭流
                        try {
                            os.close();
                        } catch (Exception e) {
                            BotConfig.logger.error(e);
                        }
                    }

                } else {
                    //提示用户生成失败
                    group.sendMessage("请不要使用不健康的词汇哦~！");
                }

            }

            inputStream = new ByteArrayInputStream(os.toByteArray());
            Image image = Contact.uploadImage(group, inputStream);
            BotConfig.logger.info("ImageId为：" + image.getImageId());
            //发送返回消息
            MessageChain sendMessage = new MessageChainBuilder()
                    .append(image)
                    .build();
            group.sendMessage(sendMessage);

            //关闭流
            try {
                os.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;

        }

    }
}
