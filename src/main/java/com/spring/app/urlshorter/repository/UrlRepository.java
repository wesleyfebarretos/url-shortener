package com.spring.app.urlshorter.repository;

import com.spring.app.urlshorter.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlEntity, Long>, CustomUrlRepository {
    @Query(
            """
            SELECT 
                u
            FROM 
                UrlEntity u
            JOIN FETCH u.user user
            WHERE
                u.originalAddress = :url
            AND
               u.expirationAt >= :now
            """
    )
    Optional<UrlEntity> findByOriginalAddressAndExpirationAtAfter(String url, ZonedDateTime now);
}