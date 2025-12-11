package com.omaden.boat.auth.application.service;

import com.omaden.boat.auth.application.dto.LoginRequest;
import com.omaden.boat.auth.application.dto.LoginResponse;
import com.omaden.boat.auth.domain.exception.InvalidCredentialsException;
import com.omaden.boat.auth.domain.model.User;
import com.omaden.boat.auth.domain.repository.UserRepository;
import com.omaden.boat.auth.infrastructure.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthenticateUserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider,
            RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    public LoginResponse authenticate(LoginRequest loginRequest) {
        User user =
                userRepository
                        .findByUsername(loginRequest.getUsername())
                        .orElseThrow(InvalidCredentialsException::new);

        User.PasswordEncoder encoder = passwordEncoder::matches;
        if (!user.verifyPassword(loginRequest.getPassword(), encoder)) {
            throw new InvalidCredentialsException();
        }

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole());
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
        return new LoginResponse(token, refreshToken);
    }
}
