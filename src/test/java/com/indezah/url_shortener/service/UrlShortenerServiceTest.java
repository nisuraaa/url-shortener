package com.indezah.url_shortener.service;

import com.indezah.url_shortener.entity.Url;
import com.indezah.url_shortener.exception.UrlAlreadyExistsException;
import com.indezah.url_shortener.exception.UrlNotFoundException;
import com.indezah.url_shortener.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlShortenerServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private ClickService clickService;

    @InjectMocks
    private UrlShortenerService urlShortenerService;

    @Test
    void getUrl_whenCodeNotFound_throwsUrlNotFoundException() {
        when(urlRepository.findByShortCode("missing")).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> urlShortenerService.getUrl("missing"));
    }

    @Test
    void getUrl_whenCodeFound_returnsOriginalUrl() {
        Url url = new Url();
        url.setShortCode("abc");
        url.setOriginalUrl("www.google.lk");

        when(urlRepository.findByShortCode("abc")).thenReturn(Optional.of(url));

        assertEquals("www.google.lk", urlShortenerService.getUrl("abc"));
        verify(clickService).recordClick("abc");
    }

    @Test
    void updateUrl_whenTargetUrlOwnedByAnotherRow_throwsAlreadyExists() {
        Url url1 = new Url();
        url1.setId(1L);
        url1.setShortCode("abc");
        url1.setOriginalUrl("www.google.lk");

        Url url2 = new Url();
        url2.setId(2L);
        url2.setShortCode("xyz");
        url2.setOriginalUrl("www.apple.com");

        when(urlRepository.findByShortCode("abc")).thenReturn(Optional.of(url1));
        when(urlRepository.findByOriginalUrl("www.apple.com")).thenReturn(Optional.of(url2));

        assertThrows(UrlAlreadyExistsException.class, () -> urlShortenerService.updateUrl("abc", "www.apple.com"));
    }

    @Test
    void createShortUrl_whenUrlAlreadyExists_returnsExistingShortCode() {
        Url existing = new Url();
        existing.setShortCode("abc");
        existing.setOriginalUrl("www.google.lk");

        when(urlRepository.findByOriginalUrl("www.google.lk")).thenReturn(Optional.of(existing));

        assertEquals("abc", urlShortenerService.createShortUrl("www.google.lk"));
        verify(urlRepository, never()).save(any());
    }

    @Test
    void updateUrl_whenShortCodeNotFound_throwsUrlNotFoundException() {
        when(urlRepository.findByShortCode("missing")).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> urlShortenerService.updateUrl("missing", "www.apple.com"));
    }
}
