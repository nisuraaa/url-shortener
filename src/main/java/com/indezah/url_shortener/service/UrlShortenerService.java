package com.indezah.url_shortener.service;

import com.indezah.url_shortener.entity.Url;
import com.indezah.url_shortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UrlShortenerService {

    private final UrlRepository urlRepository;

    public UrlShortenerService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String createShortUrl(String longUrl) {
        Optional<Url> result = urlRepository.findByOriginalUrl(longUrl);
        if (result.isPresent()) {
            Url existingUrl = result.get();
            return existingUrl.getShortCode();
        } else {
            Url newUrl = new Url();
            newUrl.setOriginalUrl(longUrl);
            newUrl.setShortCode(generateUniqueLink());
            urlRepository.save(newUrl);
            return newUrl.getShortCode();
        }
    }

    public String getUrl(String shortUrl) {
        return urlRepository.findByShortCode(shortUrl).map(Url::getOriginalUrl).orElseThrow(() -> new RuntimeException("Short URL not found"));
    }

    public Boolean updateUrl(String shortUrl, String longUrl) {
        Url result = urlRepository.findByShortCode(shortUrl).orElseThrow(() -> new RuntimeException("Short URL not found"));

        result.setOriginalUrl(longUrl);
        urlRepository.save(result);
        return true;
    }

    private String generateUniqueLink() {
        int requiredLength = 8;
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder reqString = new StringBuilder();
        Random random = new Random();


        for (int i = 0; i < requiredLength; i++) {
            int index = random.nextInt(alphabet.length());
            reqString.append(alphabet.charAt(index));
        }

        return reqString.toString();
    }
}
