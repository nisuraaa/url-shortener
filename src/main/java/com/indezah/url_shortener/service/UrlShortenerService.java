package com.indezah.url_shortener.service;

import com.indezah.url_shortener.entity.Url;
import com.indezah.url_shortener.exception.UrlAlreadyExistsException;
import com.indezah.url_shortener.exception.UrlNotFoundException;
import com.indezah.url_shortener.repository.UrlRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlShortenerService {

    private final UrlRepository urlRepository;
    private final ClickService clickService;
    private static final Logger log = LoggerFactory.getLogger(UrlShortenerService.class);
    private final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final int requiredLength = 8;
    private final int MAX_ATTEMPTS = 10;
    private final Random random = new SecureRandom();

    public UrlShortenerService(UrlRepository urlRepository, ClickService clickService) {
        this.urlRepository = urlRepository;
        this.clickService = clickService;
    }

    @Transactional
    public String createShortUrl(String originalUrl) {
        Optional<Url> existing = urlRepository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            Url existingUrl = existing.get();
            return existingUrl.getShortCode();
        }
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            Url newUrl = new Url();
            newUrl.setOriginalUrl(originalUrl);
            newUrl.setShortCode(generateUniqueCode());

            try {
                urlRepository.save(newUrl);
                return newUrl.getShortCode();
            } catch (DataIntegrityViolationException e) {
                Optional<Url> concurrentInsert = urlRepository.findByOriginalUrl(originalUrl);
                if (concurrentInsert.isPresent()) {
                    return concurrentInsert.get().getShortCode();
                }
                log.warn("Short code collision on attempt {}: {}", i, newUrl.getShortCode());
            }
        }
        throw new RuntimeException("Failed to generate a unique code after " + MAX_ATTEMPTS + " attempts");
    }


    @Transactional
    public String getUrl(String shortCode) {
        Optional<Url> url = urlRepository.findByShortCode(shortCode);
        if (url.isPresent()) {
            clickService.recordClick(shortCode);
            return url.get().getOriginalUrl();
        } else {
            throw new UrlNotFoundException(shortCode);
        }
    }

    public Url getStatistics(String shortCode) {
        Optional<Url> url = urlRepository.findByShortCode(shortCode);
        if (url.isPresent()) {
            return url.get() ;
        } else {
            throw new UrlNotFoundException(shortCode);
        }
    }

    @Transactional
    public void updateUrl(String shortCode, String originalUrl) {
        Url target = urlRepository.findByShortCode(shortCode).orElseThrow(() -> new UrlNotFoundException("Short URL not found"));
        urlRepository.findByOriginalUrl(originalUrl).ifPresent(existing -> {
            if (!existing.getId().equals(target.getId())) {
                throw new UrlAlreadyExistsException("URL Already in use");
            }
        });

        target.setOriginalUrl(originalUrl);
        urlRepository.save(target);
    }

    @Transactional
    public void deleteUrl(String shortCode) {
        Url target = urlRepository.findByShortCode(shortCode).orElseThrow(() -> new UrlNotFoundException("Short URL not found"));
        urlRepository.delete(target);
    }

    private String generateUniqueCode() {
        StringBuilder reqString = new StringBuilder();
        for (int i = 0; i < requiredLength; i++) {
            int index = random.nextInt(alphabet.length());
            reqString.append(alphabet.charAt(index));
        }

        return reqString.toString();
    }

    @Async
    public void recordClick(String shortCode) {
        urlRepository.incrementClicks(shortCode);
    }
}
