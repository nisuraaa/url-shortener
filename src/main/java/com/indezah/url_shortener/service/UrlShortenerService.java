package com.indezah.url_shortener.service;

import com.indezah.url_shortener.entity.Url;
import com.indezah.url_shortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlShortenerService {

    private final UrlRepository urlRepository;

    public UrlShortenerService(UrlRepository urlRepository){
        this.urlRepository = urlRepository;
    }

    public String createShortUrl(String longUrl){
        Optional<Url> result = urlRepository.findByOriginalUrl(longUrl);
        if(result.isPresent()){
            Url existingUrl = result.get();
            return existingUrl.getShortCode();
        } else {
            Url newUrl = new Url();
            newUrl.setOriginalUrl(longUrl);
            newUrl.setShortCode("ABC123");
            urlRepository.save(newUrl);
            return newUrl.getShortCode();
        }
    }
}
