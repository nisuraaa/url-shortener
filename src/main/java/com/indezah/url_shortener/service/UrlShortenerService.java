package com.indezah.url_shortener.service;

import com.indezah.url_shortener.entity.Url;
import com.indezah.url_shortener.exception.UrlAlreadyExistsException;
import com.indezah.url_shortener.exception.UrlNotFoundException;
import com.indezah.url_shortener.repository.UrlRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlShortenerService {

    private final UrlRepository urlRepository;
    private static final Logger log = LoggerFactory.getLogger(UrlShortenerService.class);
    private final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final int requiredLength = 8;
    private final int MAX_ATTEMPTS = 10;
    private final Random random = new SecureRandom();

    public UrlShortenerService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Transactional
    public String createShortUrl(String longUrl) {
        Optional<Url> existing = urlRepository.findByOriginalUrl(longUrl);
        if (existing.isPresent()) {
            Url existingUrl = existing.get();
            return existingUrl.getShortCode();
        }
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            Url newUrl = new Url();
            newUrl.setOriginalUrl(longUrl);
            newUrl.setShortCode(generateUniqueCode());

            try {
                urlRepository.save(newUrl);
                return newUrl.getShortCode();
            } catch (DataIntegrityViolationException e) {
                Optional<Url> concurrentInsert = urlRepository.findByOriginalUrl(longUrl);
                if (concurrentInsert.isPresent()) {
                    return concurrentInsert.get().getShortCode();
                }
                log.warn("Short code collision on attempt {}: {}", i, newUrl.getShortCode());
            }
        }
        throw new RuntimeException("Failed to generate a unique code after " + MAX_ATTEMPTS + " attempts");
    }


    public String getUrl(String shortUrl) {
        return urlRepository.findByShortCode(shortUrl).map(Url::getOriginalUrl).orElseThrow(() -> new UrlNotFoundException("Short URL not found"));
    }

    @Transactional
    public void updateUrl(String shortUrl, String longUrl) {
        Url target = urlRepository.findByShortCode(shortUrl).orElseThrow(() -> new UrlNotFoundException("Short URL not found"));
        urlRepository.findByOriginalUrl(longUrl).ifPresent(existing -> {
            if(!existing.getId().equals(target.getId())){
                throw new UrlAlreadyExistsException("URL Already in use");
            }
        });

        target.setOriginalUrl(longUrl);
        urlRepository.save(target);
    }

    private String generateUniqueCode() {
        StringBuilder reqString = new StringBuilder();
        for (int i = 0; i < requiredLength; i++) {
            int index = random.nextInt(alphabet.length());
            reqString.append(alphabet.charAt(index));
        }

        return reqString.toString();
    }
}
