package com.spring.app.urlshorter.testutils;

import com.spring.app.urlshorter.entity.UserEntity;
import com.spring.app.urlshorter.repository.UserRepository;
import com.spring.app.urlshorter.util.JwtUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TestUtils {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @ToString
    @Getter
    public static class UserData {
        private final Long id;
        private final String firstName;
        private final String lastName;
        private final String userName;
        private final String password;
        private final String token;

        public UserData(
                Long id,
                String firstName,
                String lastName,
                String userName,
                String password,
                String token
        ) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.userName = userName;
            this.password = password;
            this.token = token;
        }
    }

    public UserData createUser() {
        UserEntity newUserEntity = UserEntity.builder()
                .userName("test@gmail.com")
                .firstName("test")
                .lastName("test")
                .password("test")
                .build();

        UserEntity user = this.userRepository.save(newUserEntity);

        // Generate token if required
        String token = this.jwtUtil.generateToken(Map.of(
                "id", user.getId(),
                "email", user.getUserName()));

        Long id = user.getId();

        return new UserData(id, user.getFirstName(), user.getLastName(), user.getUserName(), user.getPassword(), token);
    }

}