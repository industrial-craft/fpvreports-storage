package com.rade.protect.service.impl;

import com.rade.protect.api.validation.exception.InvalidRefreshTokenException;
import com.rade.protect.data.RefreshTokenRepository;
import com.rade.protect.model.entity.FPVPilot;
import com.rade.protect.model.entity.RefreshToken;
import com.rade.protect.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    FPVPilotServiceImpl fpvPilotService;

    @Override
    public RefreshToken createRefreshToken(String username) {
        FPVPilot fpvPilot = fpvPilotService.findByUsername(username);
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByFpvPilot(fpvPilot);

        RefreshToken refreshToken;
        if (existingToken.isPresent()) {
            refreshToken = existingToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(600000));
        } else {
            refreshToken = RefreshToken.builder()
                    .fpvPilot(fpvPilot)
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(600000))
                    .build();
        }
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteRefreshTokenByFpvPilot(FPVPilot fpvPilot) {
        refreshTokenRepository.deleteByFpvPilot(fpvPilot);
    }

    @Transactional
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token == null) {
            throw new InvalidRefreshTokenException("Refresh token is null");
        }

        Instant now = Instant.now();
        Instant expiryDate = token.getExpiryDate();

        if (expiryDate == null || expiryDate.compareTo(now) < 0) {
            log.info("Token expired - Token: {}, Expiry: {}, Now: {}", token.getToken(), expiryDate, now);
            try {
                refreshTokenRepository.delete(token);
                log.info("Expired token deleted: {}", token.getToken());
            } catch (Exception e) {
                log.error("Failed to delete expired token: {}", token.getToken(), e);
            }
            throw new InvalidRefreshTokenException("Refresh token - " + token.getToken() + " is expired. Please make a new login!");
        }
        return token;
    }
}
