package com.indezah.url_shortener.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUrlRequest {
    private String shortUrl;
    private String newOriginalUrl;

    public UpdateUrlRequest(String shortUrl, String newOriginalUrl){
        this.newOriginalUrl = newOriginalUrl;
        this.shortUrl = shortUrl;
    }
}
