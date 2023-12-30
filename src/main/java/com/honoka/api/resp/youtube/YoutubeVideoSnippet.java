package com.honoka.api.resp.youtube;

import lombok.Data;

@Data
public class YoutubeVideoSnippet {

    private String publishedAt;

    private String channelId;

    private String title;

    private String description;

    private YoutubeVideoSnippetThumbnails thumbnails;

    private String channelTitle;

    private String categoryId;

    private String liveBroadcastContent;

    private Object localized;

    private String defaultAudioLanguage;

}
