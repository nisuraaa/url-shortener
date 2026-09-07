package com.indezah.url_shortener.controller;

import com.indezah.url_shortener.dto.*;
import com.indezah.url_shortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/url-shortener")
public class UrlController {
    private UrlShortenerService urlShortenerService;

    public UrlController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping
    public ResponseEntity<CreateUrlResponse> createUrl(@Valid @RequestBody CreateUrlRequest request) {
        String shortUrl = urlShortenerService.createShortUrl(request.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateUrlResponse(shortUrl, request.getOriginalUrl()));
    }

    @GetMapping("/{url}")
    public ResponseEntity<GetUrlResponse> getUrl(@PathVariable String url) {
        String originalUrl = urlShortenerService.getUrl(url);
        return ResponseEntity.status(HttpStatus.OK).body(new GetUrlResponse(originalUrl));
    }

    @PutMapping
    public ResponseEntity<CreateUrlResponse> updateUrl(@Valid @RequestBody UpdateUrlRequest request) {
        urlShortenerService.updateUrl(request.getShortUrl(), request.getNewOriginalUrl());
        return ResponseEntity.status(HttpStatus.OK).body(new CreateUrlResponse(request.getShortUrl(), request.getNewOriginalUrl()));
    }
}
