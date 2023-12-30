package com.honoka.api.resp;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description Pixiv结果data实体
 * @author Honoka
 * @data 2023-02-01 22:51:21
 */
@Data
public class PixivDataResp {
    private Integer pid;
    private Integer p;
    private Integer uid;
    private String title;
    private String author;
    private Boolean r18;
    private Integer width;
    private Integer height;
    private List<String> tags;
    private String ext;
    private String aiType;
    private Date uploadDate;
    private JSONObject urls;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getP() {
        return p;
    }

    public void setP(Integer p) {
        this.p = p;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getR18() {
        return r18;
    }

    public void setR18(Boolean r18) {
        this.r18 = r18;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getAiType() {
        return aiType;
    }

    public void setAiType(String aiType) {
        this.aiType = aiType;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public JSONObject getUrls() {
        return urls;
    }

    public void setUrls(JSONObject urls) {
        this.urls = urls;
    }
}
