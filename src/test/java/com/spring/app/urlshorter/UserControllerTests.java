package com.spring.app.urlshorter;

import com.spring.app.urlshorter.controller.user.AuthRequest;
import com.spring.app.urlshorter.controller.user.SaveUserRequest;
import com.spring.app.urlshorter.entity.UserEntity;
import com.spring.app.urlshorter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserControllerTests extends BaseIntegrationTests {
    private final UserRepository userRepository;

    @Nested
    class CreateUser {
        @Test
        @DisplayName("it should save an user")
        public void save() throws Exception {
            SaveUserRequest req = new SaveUserRequest(
                    "test",
                    "testing",
                    "testing@gmail.com",
                    "123"
            );

            mockMvc.perform(post("/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(req))
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.firstName").value(req.firstName()))
                    .andExpect(jsonPath("$.lastName").value(req.lastName()))
                    .andExpect(jsonPath("$.userName").value(req.userName()));

            UserEntity user = userRepository.findByUserName(req.userName())
                    .orElseThrow();

            assertThat(user).extracting(UserEntity::getUserName, UserEntity::getLastName, UserEntity::getFirstName)
                    .contains(req.userName(), req.lastName(), req.firstName());
        }

        @Test
        @DisplayName("it should not save an user, cause has duplicated username")
        public void notSave() throws Exception {
            SaveUserRequest req = new SaveUserRequest(
                    "test",
                    "testing",
                    "testing@gmail.com",
                    "123"
            );

            userRepository.save(UserEntity.builder()
                    .firstName(req.firstName())
                    .lastName(req.lastName())
                    .userName(req.userName())
                    .password(req.password())
                    .build()
            );

            mockMvc.perform(post("/user")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(req))
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                    .andExpect(jsonPath("$.msg").value("username already exists"));

            assertThat(userRepository.findAll().size()).isEqualTo(1);
        }
    }

    @Nested
    class Auth {
        @Test
        @DisplayName("it should authenticate an user")
        public void auth() throws Exception {
            SaveUserRequest req = new SaveUserRequest(
                    "test",
                    "testing",
                    "testing@gmail.com",
                    "123"
            );

            userRepository.save(UserEntity.builder()
                    .firstName(req.firstName())
                    .lastName(req.lastName())
                    .userName(req.userName())
                    .password(req.password())
                    .build()
            );

            AuthRequest authReq = new AuthRequest(req.userName(), req.password());

            mockMvc.perform(post("/user/auth")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(authReq))
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").isNotEmpty());
        }

        @Test
        @DisplayName("it should not authenticate an user")
        public void notAuth() throws Exception {
            SaveUserRequest req = new SaveUserRequest(
                    "test",
                    "testing",
                    "testing@gmail.com",
                    "123"
            );

            userRepository.save(UserEntity.builder()
                    .firstName(req.firstName())
                    .lastName(req.lastName())
                    .userName(req.userName())
                    .password(req.password())
                    .build()
            );

            AuthRequest authReq = new AuthRequest(req.userName(), req.password().concat("abc"));

            mockMvc.perform(post("/user/auth")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(authReq))
                    )
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.code").value(HttpStatus.UNAUTHORIZED.value()))
                    .andExpect(jsonPath("$.msg").value("wrong password or username"));
        }
    }
}