package com.rade.protect.service;

import com.rade.protect.model.request.FPVPilotSignInRequest;
import com.rade.protect.model.request.FPVPilotSignUpRequest;
import com.rade.protect.model.request.RefreshTokenRequest;
import com.rade.protect.model.response.FPVPilotSignInResponse;
import com.rade.protect.model.response.FPVPilotSignUpResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.HttpRequestHandler;

public interface FPVPilotAuthService {

    FPVPilotSignUpResponse signUp(FPVPilotSignUpRequest request);

    FPVPilotSignInResponse signIn(FPVPilotSignInRequest request);

    FPVPilotSignInResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void logOut(HttpServletRequest request);

}
