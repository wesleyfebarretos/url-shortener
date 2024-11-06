package com.spring.app.urlshorter;

import com.spring.app.urlshorter.controller.url.SaveUrlRequest;
import com.spring.app.urlshorter.entity.UrlEntity;
import com.spring.app.urlshorter.repository.UrlRepository;
import com.spring.app.urlshorter.testutils.TestUtils;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
        @DisplayName("it should save an url")
        public void save() throws Exception {
            TestUtils.UserData user = testUtils.createUser();

            System.out.println(user);
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

            assertThat(urls.size()).isEqualTo(1);
        }
//
//        @Test
//        @DisplayName("it should not save an user, cause has duplicated username")
//        public void notSave() throws Exception {
//            SaveUserRequest req = new SaveUserRequest(
//                    "test",
//                    "testing",
//                    "testing@gmail.com",
//                    "123"
//            );
//
//            urlRepository.save(UserEntity.builder()
//                    .firstName(req.firstName())
//                    .lastName(req.lastName())
//                    .userName(req.userName())
//                    .password(req.password())
//                    .build()
//            );
//
//            mockMvc.perform(post("/user")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(asJsonString(req))
//                    )
//                    .andDo(print())
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
//                    .andExpect(jsonPath("$.msg").value("username already exists"));
//
//            assertThat(urlRepository.findAll().size()).isEqualTo(1);
//        }
//    }
//
//    @Nested
//    class Auth {
//        @Test
//        @DisplayName("it should authenticate an user")
//        public void auth() throws Exception {
//            SaveUserRequest req = new SaveUserRequest(
//                    "test",
//                    "testing",
//                    "testing@gmail.com",
//                    "123"
//            );
//
//            urlRepository.save(UserEntity.builder()
//                    .firstName(req.firstName())
//                    .lastName(req.lastName())
//                    .userName(req.userName())
//                    .password(req.password())
//                    .build()
//            );
//
//            AuthRequest authReq = new AuthRequest(req.userName(), req.password());
//
//            mockMvc.perform(post("/user/auth")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(asJsonString(authReq))
//                    )
//                    .andDo(print())
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.token").isNotEmpty());
//        }
//
//        @Test
//        @DisplayName("it should not authenticate an user")
//        public void notAuth() throws Exception {
//            SaveUserRequest req = new SaveUserRequest(
//                    "test",
//                    "testing",
//                    "testing@gmail.com",
//                    "123"
//            );
//
//            urlRepository.save(UserEntity.builder()
//                    .firstName(req.firstName())
//                    .lastName(req.lastName())
//                    .userName(req.userName())
//                    .password(req.password())
//                    .build()
//            );
//
//            AuthRequest authReq = new AuthRequest(req.userName(), req.password().concat("abc"));
//
//            mockMvc.perform(post("/user/auth")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(asJsonString(authReq))
//                    )
//                    .andDo(print())
//                    .andExpect(status().isUnauthorized())
//                    .andExpect(jsonPath("$.code").value(HttpStatus.UNAUTHORIZED.value()))
//                    .andExpect(jsonPath("$.msg").value("wrong password or username"));
//        }
    }
}