package com.example.springsecurity.controller;

import com.example.springsecurity.dto.request.AuthenticateRequest;
import com.example.springsecurity.dto.request.RegisterRequest;
import com.example.springsecurity.dto.response.ApiResponse;
import com.example.springsecurity.dto.response.AuthenticateResponse;
import com.example.springsecurity.model.Users;
import com.example.springsecurity.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ApiResponse<Users> register(
            @RequestBody @Valid RegisterRequest user
    ) {
        var userRegister = authenticationService.register(user);

        return ApiResponse.<Users>builder()
                .result(userRegister)
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticateResponse> login(
            @RequestBody AuthenticateRequest user
    ) {
        boolean userRegister = authenticationService.authenticate(user);

        return ApiResponse.<AuthenticateResponse>builder()
                .result(AuthenticateResponse.builder()
                        .authenticated(userRegister)
                        .build())
                .build();
    }
}
