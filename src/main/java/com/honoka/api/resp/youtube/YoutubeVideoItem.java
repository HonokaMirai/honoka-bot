package com.honoka.api.resp.youtube;

import lombok.Data;

@Data
public class YoutubeVideoItem {

    private String kind;

    private String etag;

    private String id;

    private YoutubeVideoSnippet snippet;
}
