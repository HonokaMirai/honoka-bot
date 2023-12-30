package com.honoka.api;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.honoka.api.req.StableDiffusionApiConfigReq;
import com.honoka.api.req.StableDiffusionApiReq;
import com.honoka.api.resp.ChatGptPromptResp;
import com.honoka.api.resp.StableDiffusionApiModelListResp;
import com.honoka.api.resp.StableDiffusionApiResp;
import com.honoka.config.BotConfig;
import com.honoka.mirai.config.OpenAiGptConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description 调用StableDiffusion的API
 * @author honoka
 * @date 2023-03-30 21:40:19
 */
public class StableDiffusionApi {

    private static final String TXT2IMG_URL = OpenAiGptConfig.INSTANCE.SD_URL.get() + "/sdapi/v1/txt2img";

    private static final String CONFIG_URL = OpenAiGptConfig.INSTANCE.SD_URL.get() + "/sdapi/v1/options";

    private static final String MODELS_URL = OpenAiGptConfig.INSTANCE.SD_URL.get() + "/sdapi/v1/sd-models";

    private static final String REFRESH_CHECKPOINTS_URL = OpenAiGptConfig.INSTANCE.SD_URL.get() + "/sdapi/v1/refresh-checkpoints";


    public static String postTxt2ImgByGpt(ChatGptPromptResp chatGptPromptVO) {
        //封装参数
        StableDiffusionApiReq request = new StableDiffusionApiReq();
        request.setPrompt("(masterpiece:1.6), (best quality:1.4), 4k, " + chatGptPromptVO.getPrompt());
        request.setNegativePrompt("easynegative, (NSFW:1.6), watermark" + chatGptPromptVO.getNegativePrompt());
        request.setSamplerName("Euler a");
        request.setSamplerIndex("Euler");
        request.setWidth(chatGptPromptVO.getWidth() == null || chatGptPromptVO.getWidth() > 2048 ? 512 : chatGptPromptVO.getWidth());
        request.setHeight(chatGptPromptVO.getHeight() == null || chatGptPromptVO.getHeight() > 2048 ? 512 : chatGptPromptVO.getHeight());

        StableDiffusionApiResp stableDiffusionApiVO = postTxt2Img(request);
        if (stableDiffusionApiVO == null) {
            return null;
        }
        return stableDiffusionApiVO.getImages();
    }

    public static StableDiffusionApiResp postTxt2Img(StableDiffusionApiReq request) {
        //参数校验
        request.setEnableHr(false);

        request.setSamplerName(StringUtils.isBlank(request.getSamplerName()) ? "Euler a" : request.getSamplerName());
        request.setSamplerIndex(StringUtils.isBlank(request.getSamplerIndex()) ? "Euler" : request.getSamplerIndex());
        request.setBatchSize(1);
        request.setnIter(1);
        request.setSteps(request.getSteps() < 1 ? 20: request.getSteps());
        request.setCfgScale(request.getCfgScale() < 1 ? 7 : request.getCfgScale());
        request.setWidth(request.getWidth() < 1 || request.getWidth() > 2048 ? 512 : request.getWidth());
        request.setHeight(request.getHeight() < 1 || request.getHeight() > 2048 ? 512 : request.getHeight());
        request.setRestoreFaces(false);
        request.setTiling(false);
        request.setDoNotSaveSamples(false);
        request.setDoNotSaveGrid(false);
        request.setsNoise(1);
        request.setSeed(request.getSeed() == null ? -1L : request.getSeed());

        request.setSendImages(true);

        //封装参数
        HttpResponse httpResponse = null;
        StableDiffusionApiResp stableDiffusionApiVO = null;
        BotConfig.logger.info("开始调用StableDiffusionApi");
        try {
            httpResponse = HttpRequest.post(TXT2IMG_URL)
                    .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                    .body(JSONObject.toJSONString(request))
                    .timeout(60000)
                    .execute();
        } catch (Exception e) {
            BotConfig.logger.error("调用StableDiffusionApi失败" + e);

        }
        if (httpResponse != null && httpResponse.getStatus() == 200) {
            String body = httpResponse.body();
            stableDiffusionApiVO = JSONObject.parseObject(body, StableDiffusionApiResp.class);
            return stableDiffusionApiVO;
        }

        return stableDiffusionApiVO;
    }

    public static List<String> getSdModelList() {
        HttpResponse httpResponse = null;
        BotConfig.logger.info("开始调用StableDiffusionApi:getSdModelList");
        try {
            httpResponse = HttpRequest.get(MODELS_URL)
                    .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                    .timeout(60000)
                    .execute();
        } catch (Exception e) {
            BotConfig.logger.error("调用StableDiffusionApi:getSdModelList失败" + e);
        }
        List<StableDiffusionApiModelListResp> apiModelList = JSONArray.parseArray(httpResponse.body(), StableDiffusionApiModelListResp.class);
        List<String> modelList = apiModelList.stream().map(StableDiffusionApiModelListResp::getTitle).collect(Collectors.toList());
        return modelList;
    }

    public static String setConfigOptions(StableDiffusionApiConfigReq configOptions) {
        HttpResponse httpResponse = null;
        BotConfig.logger.info("开始调用StableDiffusionApi:setConfigOptions" + JSONObject.toJSONString(configOptions));
        try {
            httpResponse = HttpRequest.post(CONFIG_URL)
                    .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                    .body(JSONObject.toJSONString(configOptions))
                    .timeout(60000)
                    .execute();
        } catch (Exception e) {
            BotConfig.logger.error("调用StableDiffusionApi:setConfigOptions失败" + e);

        }
        return httpResponse.body();
    }

    public static void refreshCheckpoints() {
        HttpResponse httpResponse = null;
        BotConfig.logger.info("开始调用StableDiffusionApi:refreshCheckpoints");
        try {
            httpResponse = HttpRequest.post(REFRESH_CHECKPOINTS_URL)
                    .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                    .body("")
                    .timeout(60000)
                    .execute();
        } catch (Exception e) {
            BotConfig.logger.error("调用StableDiffusionApi:refreshCheckpoints失败" + e);

        }

    }



}
