package com.rade.protect.api;

import com.rade.protect.api.validation.exception.FPVPilotNotFoundException;
import com.rade.protect.api.validation.exception.InvalidRefreshTokenException;
import com.rade.protect.model.request.FPVPilotSignInRequest;
import com.rade.protect.model.request.FPVPilotSignUpRequest;
import com.rade.protect.model.request.RefreshTokenRequest;
import com.rade.protect.model.response.FPVPilotSignInResponse;
import com.rade.protect.model.response.FPVPilotSignUpResponse;
import com.rade.protect.service.impl.FPVPilotAuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final FPVPilotAuthServiceImpl fpvPilotAuthServiceImpl;

    @PostMapping("/signup")
    public ResponseEntity<FPVPilotSignUpResponse> signUp(@RequestBody FPVPilotSignUpRequest registerUserDto) {
        FPVPilotSignUpResponse registeredUser = fpvPilotAuthServiceImpl.signUp(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody FPVPilotSignInRequest loginRequest) {
        try {
            FPVPilotSignInResponse authenticatedFPVPilotResponse = fpvPilotAuthServiceImpl.signIn(loginRequest);
            return ResponseEntity.ok(authenticatedFPVPilotResponse);
        } catch (FPVPilotNotFoundException e) {
            log.warn("Invalid fpv pilot credentials: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        } catch (BadCredentialsException e) {
            log.warn("Bad credentials: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AppError(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"));
        } catch (Exception e) {
            log.error("Unexpected error during signin: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error"));
        }
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            FPVPilotSignInResponse response = fpvPilotAuthServiceImpl.refreshToken(refreshTokenRequest);
            return ResponseEntity.ok(response);
        } catch (InvalidRefreshTokenException e) {
            log.warn("Invalid refresh token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        } catch (Exception e) {
            log.error("Error processing refresh token: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logOut(HttpServletRequest request) {
        try {
            fpvPilotAuthServiceImpl.logOut(request);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new AppError(HttpStatus.OK.value(), "Logged out successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AppError(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        } catch (FPVPilotNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }
    }
}
