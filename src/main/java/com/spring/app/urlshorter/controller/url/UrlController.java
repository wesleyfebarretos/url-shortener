package com.spring.app.urlshorter.controller.url;

import com.spring.app.urlshorter.entity.UrlEntity;
import com.spring.app.urlshorter.service.UrlService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.internal.util.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("url")
@RequiredArgsConstructor
public class UrlController {
    private final UrlService urlService;

    @PostMapping
    public SaveUrlResponse save(HttpServletRequest req, @RequestBody @Valid SaveUrlRequest url) {
        Claims claims  =(Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UrlEntity newUrl = UrlEntity.builder()
                .originalAddress(url.url())
                .userId(Long.valueOf(claims.get("id").toString()))
                .build();

        String scheme = req.getScheme();             // http or https
        String serverName = req.getServerName();     // domain or IP
        int serverPort = req.getServerPort();        // port number

        UrlEntity newEntity =  urlService.save(newUrl);

        String shortUrl = scheme + "://" + serverName + (serverPort != 80 && serverPort != 443 ? ":" + serverPort : "");

        return new SaveUrlResponse(shortUrl.concat("/" + newEntity.getShortCode()));
    }
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> findOneByShortCode(@PathVariable String shortCode) {
        UrlEntity url = urlService.findByShortCode(shortCode);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url.getOriginalAddress()))
                .build();
    }
}
