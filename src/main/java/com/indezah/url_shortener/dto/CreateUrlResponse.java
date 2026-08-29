package com.indezah.url_shortener.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUrlResponse {
    private String shortUrl;
    private String originalUrl;

    public CreateUrlResponse(String shortUrl, String originalUrl){
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
    }
}
