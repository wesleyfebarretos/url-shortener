package com.spring.app.urlshorter.service;

import com.spring.app.urlshorter.entity.UrlEntity;
import com.spring.app.urlshorter.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;

    public List<UrlEntity> findAll() {
        return urlRepository.findAll();
    }

    public UrlEntity findByShortCode(String shortCode) {
        return urlRepository.updateAndFindByCode(shortCode)
                .orElseThrow(() -> new RuntimeException("short code not found"));
    }

    public UrlEntity save(UrlEntity url) {
        Optional<UrlEntity> oldUrl = urlRepository.findByOriginalAddressAndExpirationAtAfter(
                url.getOriginalAddress(),
                ZonedDateTime.now()
        );

        if(oldUrl.isPresent()) {
            return oldUrl.get();
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(url.getOriginalAddress().getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            url.setShortCode(hexString.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found: " + e.getMessage());
        }

        url.setExpirationAt(ZonedDateTime.now().plusMinutes(5));

        return urlRepository.save(url);
    }
}
