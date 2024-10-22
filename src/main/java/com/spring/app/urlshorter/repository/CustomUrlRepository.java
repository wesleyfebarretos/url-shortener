package com.spring.app.urlshorter.repository;

import com.spring.app.urlshorter.entity.UrlEntity;

import java.util.Optional;

public interface CustomUrlRepository {
    Optional<UrlEntity> updateAndFindByCode(String shortcode);
}
