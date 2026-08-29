package com.indezah.url_shortener.controller;

import com.indezah.url_shortener.dto.CreateUrlRequest;
import com.indezah.url_shortener.dto.CreateUrlResponse;
import com.indezah.url_shortener.service.UrlShortenerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/url-shortener")
public class UrlController {
    private UrlShortenerService urlShortenerService;

    public UrlController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping
    public ResponseEntity<CreateUrlResponse> createUrl(@RequestBody CreateUrlRequest request) {
        if (request.getOriginalUrl().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String shortUrl = urlShortenerService.createShortUrl(request.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateUrlResponse(shortUrl, request.getOriginalUrl()));
    }
}
