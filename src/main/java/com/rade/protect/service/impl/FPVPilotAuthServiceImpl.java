package com.rade.protect.service.impl;

import com.rade.protect.api.validation.exception.InvalidRefreshTokenException;
import com.rade.protect.model.entity.FPVPilot;
import com.rade.protect.model.entity.RefreshToken;
import com.rade.protect.model.request.FPVPilotSignInRequest;
import com.rade.protect.model.request.FPVPilotSignUpRequest;
import com.rade.protect.model.request.RefreshTokenRequest;
import com.rade.protect.model.response.FPVPilotSignInResponse;
import com.rade.protect.model.response.FPVPilotSignUpResponse;
import com.rade.protect.service.FPVPilotAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FPVPilotAuthServiceImpl implements FPVPilotAuthService {

    private final FPVPilotServiceImpl fpvPilotServiceImpl;

    private final JwtServiceImpl jwtServiceImpl;

    private final TokenBlackListServiceImpl tokenBlackListImpl;

    private final RefreshTokenServiceImpl refreshTokenServiceImpl;

    private final AuthenticationManager authenticationManager;

    @Override
    public FPVPilotSignUpResponse signUp(FPVPilotSignUpRequest request) {
        return fpvPilotServiceImpl.register(request);
    }

    @Override
    public FPVPilotSignInResponse signIn(FPVPilotSignInRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        FPVPilot authenticatedUser = fpvPilotServiceImpl.findByUsername(request.getUsername());

        String jwtToken = jwtServiceImpl.generateToken(authenticatedUser);

        RefreshToken refreshToken = refreshTokenServiceImpl.createRefreshToken(request.getUsername());

        return mapToAuthenticateResponse(jwtToken, refreshToken);
    }

    @Override
    public void logOut(HttpServletRequest request) {
        tokenBlackListImpl.addToBlacklist(request);
        String token = jwtServiceImpl.extractTokenFromRequest(request);
        String username = jwtServiceImpl.extractUserName(token);
        FPVPilot fpvPilot = fpvPilotServiceImpl.findByUsername(username);
        refreshTokenServiceImpl.deleteRefreshTokenByFpvPilot(fpvPilot);
        SecurityContextHolder.clearContext();
    }

    @Override
    public FPVPilotSignInResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshTokenValue = refreshTokenRequest.getRefreshToken();
        if (refreshTokenValue == null || refreshTokenValue.isEmpty()) {
            throw new InvalidRefreshTokenException("Refresh token is missing");
        }

        Optional<RefreshToken> refreshTokenOpt = refreshTokenServiceImpl.findByToken(refreshTokenValue);
        if (refreshTokenOpt.isEmpty()) {
            throw new InvalidRefreshTokenException("Refresh token - " + refreshTokenValue + " is not in DB!");
        }

        RefreshToken refreshToken = refreshTokenServiceImpl.verifyExpiration(refreshTokenOpt.get());
        FPVPilot fpvPilot = refreshToken.getFpvPilot();
        String accessToken = jwtServiceImpl.generateToken(fpvPilot);

        return mapToSignInResponseRefreshToken(accessToken, refreshTokenRequest);
    }

    public FPVPilotSignInResponse mapToSignInResponseRefreshToken(String accessToken, RefreshTokenRequest refreshTokenRequest) {
        return FPVPilotSignInResponse.builder()
                .token(accessToken)
                .expiresIn(jwtServiceImpl.getExpirationTime())
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .build();
    }

    public FPVPilotSignInResponse mapToAuthenticateResponse(String jwtToken, RefreshToken refreshToken) {
        return FPVPilotSignInResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtServiceImpl.getExpirationTime())
                .refreshToken(refreshToken.getToken())
                .build();
    }

}
