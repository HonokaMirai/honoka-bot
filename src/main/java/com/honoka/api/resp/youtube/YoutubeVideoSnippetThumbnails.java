package com.honoka.api.resp.youtube;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class YoutubeVideoSnippetThumbnails {

    @JSONField(name = "default")
    private YoutubeVideoSnippetThumbnailsEntity _default;

    private YoutubeVideoSnippetThumbnailsEntity medium;

    private YoutubeVideoSnippetThumbnailsEntity high;

    private YoutubeVideoSnippetThumbnailsEntity standard;

    @JSONField(name = "maxres")
    private YoutubeVideoSnippetThumbnailsEntity maxres;
}
