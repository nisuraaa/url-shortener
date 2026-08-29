package com.indezah.url_shortener.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUrlRequest {
    private String originalUrl;
}
