package com.honoka.api.resp.youtube;

import lombok.Data;

import java.util.List;

@Data
public class YoutubeCommonResp {

    private String kind;

    private String etag;

    private List<YoutubeVideoItem> items;

    private YoutubeCommonPageInfo pageInfo;

}
