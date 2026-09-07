package com.indezah.url_shortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class UpdateUrlRequest {
    @NotBlank(message = "Code must not be empty")
    private String shortCode;
    @NotBlank(message = "URL must not be empty")
    @URL(message = "originalUrl must be a valid URL")
    @Pattern(regexp = "^https?://.*", message = "URL must start with http:// or https://")
    private String originalUrl;
}
