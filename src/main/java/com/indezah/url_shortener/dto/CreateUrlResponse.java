package com.indezah.url_shortener.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUrlResponse {
    private String shortCode;
    private String originalUrl;

    public CreateUrlResponse(String shortCode, String originalUrl){
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
    }
}
