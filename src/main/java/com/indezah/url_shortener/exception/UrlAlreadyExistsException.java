package com.indezah.url_shortener.exception;

public class UrlAlreadyExistsException extends RuntimeException {
    public UrlAlreadyExistsException(String message) {
        super(message);
    }
}
