package com.indezah.url_shortener.controller;

import com.indezah.url_shortener.dto.*;
import com.indezah.url_shortener.service.UrlShortenerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{url}")
    public ResponseEntity<GetUrlResponse> getUrl(@PathVariable String url) {
        try {
            String originalUrl = urlShortenerService.getUrl(url);
            return ResponseEntity.status(HttpStatus.FOUND).body(new GetUrlResponse(originalUrl));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<CreateUrlResponse> updateUrl(@RequestBody UpdateUrlRequest request) {
        try {
            Boolean status = urlShortenerService.updateUrl(request.getShortUrl(), request.getNewOriginalUrl());
            if (status) {
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            return ResponseEntity.notFound().build();

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
