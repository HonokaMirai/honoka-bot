package com.honoka.api.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @description gpt调用要求返回的格式
 * @author honoka
 * @date 2023-03-30 21:22:07
 */
@Data
public class ChatGptPromptResp {

    @JSONField(name = "code")
    private Integer code;

    @JSONField(name = "question")
    private String question;

    @JSONField(name = "model")
    private String model;

    @JSONField(name = "prompt")
    private String prompt;

    @JSONField(name = "negative_prompt")
    private String negativePrompt;

    @JSONField(name = "width")
    private Integer width;

    @JSONField(name = "height")
    private Integer height;

    @JSONField(name = "lora")
    private List<String> lora;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getNegativePrompt() {
        return negativePrompt;
    }

    public void setNegativePrompt(String negativePrompt) {
        this.negativePrompt = negativePrompt;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public List<String> getLora() {
        return lora;
    }

    public void setLora(List<String> lora) {
        this.lora = lora;
    }
}
