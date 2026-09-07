package com.indezah.url_shortener.dto;

import com.indezah.url_shortener.entity.Url;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class StatsResponse {
    private Long id;
    private String shortUrl;
    private String url;
    private LocalDateTime createdAt;

}
