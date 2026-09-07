package com.indezah.url_shortener.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UrlResponse {
    private Long id;
    private String shortCode;
    private String originalUrl;
    private LocalDateTime createdAt;
    private Integer clicks;

}
