package com.spring.app.urlshorter.repository;

import com.spring.app.urlshorter.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlEntity, Long>, CustomUrlRepository {
    Optional<UrlEntity> findByOriginalAddressAndExpirationAtAfter(String url, ZonedDateTime now);
}