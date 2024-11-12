package com.spring.app.urlshorter.service;

import com.spring.app.urlshorter.entity.UrlEntity;
import com.spring.app.urlshorter.entity.UserEntity;
import com.spring.app.urlshorter.exception.ApiException;
import com.spring.app.urlshorter.repository.UserRepository;
import com.spring.app.urlshorter.util.JwtUtil;
import com.spring.app.urlshorter.util.RepositoryHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UrlService urlService;
    private final JwtUtil jwtUtil;
    private final RepositoryHelper repositoryHelper;

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity save(UserEntity user) {
        userRepository.findByUserName(user.getUserName())
                .ifPresent((x) -> {
                    throw ApiException.badRequestException("username already exists");
                });
        return userRepository.save(user);
    }

    public UserEntity saveUserAndTestURL(UserEntity user) {
        UserEntity newUser = repositoryHelper.runInTransaction(() -> {
            return this.privateSave(user);
        });

        repositoryHelper.runInTransaction(() -> {
            this.saveUrl(UrlEntity.builder().originalAddress("http://test.com").user(newUser).build());
        });

        return newUser;
    }

    @Transactional
    private void saveUrl(UrlEntity url) {
        urlService.save(url);
    }

    @Transactional
    private UserEntity privateSave(UserEntity user) {
        UserEntity newUser = this.save(user);

        UrlEntity newUrl = UrlEntity.builder()
                .originalAddress("http://test.com")
                .user(user)
                .build();


        return newUser;
    }

    public String auth(String userName, String password) {
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() -> ApiException.unaunthorizedException("wrong password or username"));

        //  TODO: Check with encryption
        if (!password.equals(user.getPassword())) {
            throw ApiException.unaunthorizedException("wrong password or username");
        }

        return jwtUtil.generateToken(Map.of(
                "id", user.getId(),
                "email", user.getUserName())
        );
    }

}
