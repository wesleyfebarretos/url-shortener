package com.spring.app.urlshorter.controller.user;

import com.spring.app.urlshorter.entity.UserEntity;
import com.spring.app.urlshorter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public SaveUserResponse save(@RequestBody @Valid SaveUserRequest params) {
        UserEntity newUser = UserEntity.builder()
                .userName(params.userName())
                .firstName(params.firstName())
                .lastName(params.lastName())
                .password(params.password())
                .build();

        UserEntity user = userService.save(newUser);

        return new SaveUserResponse(
                user.getFirstName(),
                user.getLastName(),
                user.getUserName(),
                user.getPassword()
        );

    }

    @PostMapping("/auth")
    public String auth(@RequestBody @Valid AuthRequest params) {
        return userService.auth(params.userName(), params.password());
    }
}
