package com.spring.app.urlshorter;

import com.spring.app.urlshorter.controller.url.SaveUrlRequest;
import com.spring.app.urlshorter.entity.UrlEntity;
import com.spring.app.urlshorter.entity.UserEntity;
import com.spring.app.urlshorter.repository.UrlRepository;
import com.spring.app.urlshorter.testutils.TestUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UrlControllerTests extends BaseIntegrationTests {
    private final UrlRepository urlRepository;

    @Nested
    class CreateUrl {
        @Test
        @Rollback
        @DisplayName("it should save an url")
        public void save() throws Exception {
            TestUtils.UserData user = testUtils.createUser();

            SaveUrlRequest req = new SaveUrlRequest("https://google.com.br");

            mockMvc.perform(post("/url")
                            .header("Authorization", "Bearer  " + user.getToken())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(req))
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.shortUrl").isNotEmpty());

            UrlEntity url = urlRepository.findByOriginalAddressAndExpirationAtAfter(req.url(), ZonedDateTime.now())
                    .orElseThrow();

            List<UrlEntity> urls = urlRepository.findAll();

            assertThat(url).extracting(UrlEntity::getOriginalAddress)
                    .isEqualTo(req.url());

            assertThat(url).extracting(UrlEntity::getShortCode)
                    .isNotNull();

            assertThat(url.getExpirationAt()).isAfter(ZonedDateTime.now());

            assertThat(urls.size()).isEqualTo(1);
        }
    }

    @Nested
    class GetURL {
        @Test
        @Rollback
        @DisplayName("it should find an URL by shortCode")
        public void findByShortCode() throws Exception {
            TestUtils.UserData user = testUtils.createUser();

            UserEntity userReference = new UserEntity().setId(user.getId());

            ZonedDateTime now = ZonedDateTime.now();

            UrlEntity url = UrlEntity.builder()
                    .originalAddress("https://google.com.br")
                    .shortCode("ABCD")
                    .expirationAt(now.plusMinutes(5))
                    .user(userReference)
                    .build();

            urlRepository.save(url);

            mockMvc.perform(get("/url/".concat(url.getShortCode()))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer ".concat(user.getToken()))
                    )
                    .andDo(print())
                    .andExpect(status().is3xxRedirection());
        }
    }
}