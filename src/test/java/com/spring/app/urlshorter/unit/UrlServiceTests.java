package com.spring.app.urlshorter.unit;

import com.spring.app.urlshorter.entity.UrlEntity;
import com.spring.app.urlshorter.repository.UrlRepository;
import com.spring.app.urlshorter.service.UrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class UrlServiceTests {

    @Mock
    UrlRepository urlRepository;

    @InjectMocks
    UrlService urlService;

    @Nested
    class Save {
        @Test
        @DisplayName("it should return and new url")
        public void save() {
            UrlEntity newUrl = UrlEntity.builder()
                    .originalAddress("http://google.com.br")
                    .id(1L)
                    .accessQty(10)
                    .build();

            Mockito.when(urlRepository.findByOriginalAddressAndExpirationAtAfter(eq(newUrl.getOriginalAddress()), any()))
                    .thenReturn(Optional.empty());

            Mockito.when(urlRepository.save(newUrl)).thenAnswer(AdditionalAnswers.returnsFirstArg());

            UrlEntity url = urlService.save(newUrl);

            Mockito.verify(urlRepository, Mockito.times(1))
                    .findByOriginalAddressAndExpirationAtAfter(eq(newUrl.getOriginalAddress()), any());

            Mockito.verify(urlRepository, Mockito.times(1))
                    .save(newUrl);

            assertThat(url.getExpirationAt()).isAfter(ZonedDateTime.now());
            assertThat(url.getShortCode()).isNotEmpty();
            assertThat(url).extracting(UrlEntity::getOriginalAddress, UrlEntity::getId, UrlEntity::getAccessQty)
                    .contains(newUrl.getOriginalAddress(), newUrl.getId(), newUrl.getAccessQty());
        }
    }
}
