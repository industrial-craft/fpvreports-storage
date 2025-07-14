package com.rade.protect.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FPVPilotSignInResponse {

    private String token;

    private long expiresIn;

    private String refreshToken;

}
