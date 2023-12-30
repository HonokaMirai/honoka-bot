package com.honoka.api.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class StableDiffusionApiModelListResp {

    @JSONField(name = "title")
    private String title;

    @JSONField(name = "model_name")
    private String modelName;

    @JSONField(name = "hash")
    private String hash;

    @JSONField(name = "sha256")
    private String sha256;

    @JSONField(name = "filename")
    private String filename;

    @JSONField(name = "config")
    private String config;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
