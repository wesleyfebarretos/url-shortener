package com.spring.app.urlshorter.controller.user;

import com.spring.app.urlshorter.entity.UserEntity;
import com.spring.app.urlshorter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @Tag(name = "User")
    @Operation(summary = "Register an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "400", description = "username already exists"),
    })
    @ResponseStatus(HttpStatus.CREATED)
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
                user.getUserName()
        );

    }

    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody @Valid AuthRequest params) {
        String token = userService.auth(params.userName(), params.password());

        return new AuthResponse(token);
    }
}
