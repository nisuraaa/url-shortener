package com.indezah.url_shortener.controller;

import com.indezah.url_shortener.dto.*;
import com.indezah.url_shortener.entity.Url;
import com.indezah.url_shortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("api/url-shortener")
public class UrlController {
    private UrlShortenerService urlShortenerService;

    public UrlController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping
    public ResponseEntity<CreateUrlResponse> createUrl(@Valid @RequestBody CreateUrlRequest request) {
        String shortCode = urlShortenerService.createShortUrl(request.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateUrlResponse(shortCode, request.getOriginalUrl()));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> getUrl(@PathVariable String shortCode) {
        String originalUrl = urlShortenerService.getUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
    }

    @PutMapping
    public ResponseEntity<UpdateUrlResponse> updateUrl(@Valid @RequestBody UpdateUrlRequest request) {
        urlShortenerService.updateUrl(request.getShortCode(), request.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateUrlResponse(request.getShortCode(), request.getOriginalUrl()));
    }

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<StatsResponse> getStatistics(@PathVariable String shortCode) {
        Url statistics = urlShortenerService.getStatistics(shortCode);
        return ResponseEntity.status(HttpStatus.OK).body(new StatsResponse(statistics.getId(), statistics.getShortCode(), statistics.getOriginalUrl(), statistics.getCreatedAt()));
    }
}
