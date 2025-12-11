package com.omaden.boat.auth.application.service;

import com.omaden.boat.auth.domain.exception.InvalidCredentialsException;
import com.omaden.boat.auth.domain.model.User;
import com.omaden.boat.auth.domain.repository.UserRepository;
import com.omaden.boat.auth.infrastructure.persistence.RefreshTokenEntity;
import com.omaden.boat.auth.infrastructure.persistence.RefreshTokenRepository;
import com.omaden.boat.auth.infrastructure.security.JwtTokenProvider;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-expiration:2592000000}")
    private long jwtRefreshExpirationInMs;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Transactional
    public String createRefreshToken(String username) {
        // Delete existing refresh token for this user
        refreshTokenRepository.deleteByUsername(username);

        // Generate new refresh token
        String token = jwtTokenProvider.generateRefreshToken(username);
        Instant expiryDate = Instant.now().plusMillis(jwtRefreshExpirationInMs);

        RefreshTokenEntity refreshToken = new RefreshTokenEntity(token, username, expiryDate);
        refreshTokenRepository.save(refreshToken);

        return token;
    }

    @Transactional
    public String refreshAccessToken(String refreshToken) {
        RefreshTokenEntity tokenEntity =
                refreshTokenRepository
                        .findByToken(refreshToken)
                        .orElseThrow(InvalidCredentialsException::new);

        if (tokenEntity.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(tokenEntity);
            throw new InvalidCredentialsException();
        }

        String username = tokenEntity.getUsername();
        User user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(InvalidCredentialsException::new);
        return jwtTokenProvider.generateToken(username, user.getRole());
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    @Transactional
    public void deleteByUsername(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }
}
