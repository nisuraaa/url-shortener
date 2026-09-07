package com.indezah.url_shortener.service;

import com.indezah.url_shortener.repository.UrlRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ClickService {
    private final UrlRepository urlRepository;

    public ClickService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Async
    @Transactional
    public void recordClick(String code) {
        urlRepository.incrementClicks(code);
    }

}
