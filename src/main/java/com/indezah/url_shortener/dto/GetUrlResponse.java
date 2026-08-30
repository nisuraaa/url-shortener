package com.indezah.url_shortener.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetUrlResponse {
    private String url;

    public GetUrlResponse(String url) {
        this.url = url;
    }
}
