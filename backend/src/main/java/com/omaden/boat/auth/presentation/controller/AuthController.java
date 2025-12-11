package com.omaden.boat.auth.presentation.controller;

import com.omaden.boat.auth.application.dto.LoginRequest;
import com.omaden.boat.auth.application.dto.LoginResponse;
import com.omaden.boat.auth.application.dto.RefreshTokenRequest;
import com.omaden.boat.auth.application.service.AuthenticateUserService;
import com.omaden.boat.auth.application.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateUserService authenticateUserService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(
            AuthenticateUserService authenticateUserService,
            RefreshTokenService refreshTokenService) {
        this.authenticateUserService = authenticateUserService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authenticateUserService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        String newToken = refreshTokenService.refreshAccessToken(request.getRefreshToken());
        LoginResponse response = new LoginResponse(newToken, request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
}
