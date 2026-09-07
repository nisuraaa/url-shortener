package com.indezah.url_shortener.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUrlResponse {
    private String shortUrl;
    private String newOriginalUrl;

    public UpdateUrlResponse(String shortUrl, String newOriginalUrl) {
        this.shortUrl = shortUrl;
        this.newOriginalUrl = newOriginalUrl;
    }
}
