package com.indezah.url_shortener.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class CreateUrlRequest {
    @NotBlank(message = "URL must not be empty")
    @URL(message = "originalUrl must be a valid URL")
    private String originalUrl;
}
