package com.spring.app.urlshorter.unit;

import com.spring.app.urlshorter.entity.UrlEntity;
import com.spring.app.urlshorter.repository.UrlRepository;
import com.spring.app.urlshorter.service.UrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;

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
        public static Stream<Arguments> save() {
            return Stream.of(
                    Arguments.arguments("http://google.com.br", "cf5f259e8b672114f55089680a54c05d"),
                    Arguments.arguments("http://test.com.br", "66d82bfaed10b76f35177d03e8b4dd19")
            );
        }

        @ParameterizedTest
        @MethodSource("save")
        @DisplayName("it should return and new url")
        public void save(String urlArg, String expected) {

            UrlEntity newUrl = UrlEntity.builder()
                    .originalAddress(urlArg)
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
            assertThat(url.getShortCode()).isEqualTo(expected);
            assertThat(url).extracting(UrlEntity::getOriginalAddress, UrlEntity::getId, UrlEntity::getAccessQty)
                    .contains(newUrl.getOriginalAddress(), newUrl.getId(), newUrl.getAccessQty());
        }
    }
}
