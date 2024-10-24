package com.spring.app.urlshorter.repository;

import com.spring.app.urlshorter.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserName(String userName);
    // Implement methods in interface
    default Optional<UserEntity> customFind(String userName) {
        return findByUserName(userName);
    };
}