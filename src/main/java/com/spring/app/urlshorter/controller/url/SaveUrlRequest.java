package com.spring.app.urlshorter.controller.url;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SaveUrlRequest(
        @NotBlank(message = "URL must not be blank")
        @Pattern(
                regexp = "^(https?://)?(www\\.)?([\\w-]+\\.)+[\\w-]+(/[^\\s]*)?$",
                message = "URL must be valid"
        )
        String url
){};
