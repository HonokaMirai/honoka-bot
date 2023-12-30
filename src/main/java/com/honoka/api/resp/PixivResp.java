package com.honoka.api.resp;

import lombok.Data;

import java.util.List;

/**
 * @description P站对象返回体
 * @author honoka
 * @date 2023-02-01 22:49:40
 */
@Data
public class PixivResp {

    private String error;

    private List<PixivDataResp> data;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<PixivDataResp> getData() {
        return data;
    }

    public void setData(List<PixivDataResp> data) {
        this.data = data;
    }
}
