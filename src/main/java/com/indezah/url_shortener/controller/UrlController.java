package com.indezah.url_shortener.controller;

import com.indezah.url_shortener.dto.*;
import com.indezah.url_shortener.entity.Url;
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
        String shortCode = urlShortenerService.createShortUrl(request.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateUrlResponse(shortCode, request.getOriginalUrl()));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<UrlResponse> getUrl(@PathVariable String shortCode) {
        Url url = urlShortenerService.getUrl(shortCode);
        return ResponseEntity.status(HttpStatus.OK).body(new UrlResponse(url.getId(), url.getShortCode(), url.getOriginalUrl(), url.getCreatedAt(), url.getClicks()));
    }

    @PutMapping("/{shortCode}")
    public ResponseEntity<UpdateUrlResponse> updateUrl(@Valid @RequestBody UpdateUrlRequest request, @PathVariable String shortCode) {
        urlShortenerService.updateUrl(shortCode, request.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateUrlResponse(shortCode, request.getOriginalUrl()));
    }

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<UrlResponse> getStatistics(@PathVariable String shortCode) {
        Url statistics = urlShortenerService.getStatistics(shortCode);
        return ResponseEntity.status(HttpStatus.OK).body(new UrlResponse(statistics.getId(), statistics.getShortCode(), statistics.getOriginalUrl(), statistics.getCreatedAt(), statistics.getClicks()));
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortCode) {
        urlShortenerService.deleteUrl(shortCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
