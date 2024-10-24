package com.spring.app.urlshorter.controller.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record SaveUserRequest(
        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotBlank
        @Email
        String userName,

        @NotBlank
        @Length(max = 100, min = 3)
        String password
) {
}
