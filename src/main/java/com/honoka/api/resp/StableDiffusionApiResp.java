package com.honoka.api.resp;

import com.alibaba.fastjson.annotation.JSONField;
import com.honoka.api.req.StableDiffusionApiReq;
import lombok.Data;

@Data
public class StableDiffusionApiResp {

    @JSONField(name = "images")
    private String images;

    @JSONField(name = "parameters")
    private StableDiffusionApiReq parameters;

    @JSONField(name = "info")
    private String info;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public StableDiffusionApiReq getParameters() {
        return parameters;
    }

    public void setParameters(StableDiffusionApiReq parameters) {
        this.parameters = parameters;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
