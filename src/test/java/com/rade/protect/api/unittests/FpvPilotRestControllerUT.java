package com.rade.protect.api.unittests;

import com.rade.protect.model.Role;
import com.rade.protect.model.entity.FPVPilot;
import com.rade.protect.model.request.FPVPilotAdminRequest;
import com.rade.protect.model.response.FPVPilotSignUpResponse;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;

public class FpvPilotRestControllerUT {

    protected FPVPilotAdminRequest fpvPilotAdminRequest;

    protected FPVPilotAdminRequest fpvPilotUserRequest;

    protected void givenValidFpvPilotAdminRequest() {
        fpvPilotAdminRequest = FPVPilotAdminRequest.builder()
                .username("samsep10l")
                .firstname("Elliot")
                .lastname("Alderson")
                .password("fsociety")
                .authorities(Role.ADMIN.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                .build();
    }

    protected void givenValidFpvPilotUserRequest() {
        fpvPilotUserRequest = FPVPilotAdminRequest.builder()
                .username("chaikin1")
                .firstname("Darlene")
                .lastname("Alderson")
                .password("fsociety-sister")
                .authorities(Role.USER.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                .build();
    }

    public FPVPilotSignUpResponse mapToCreateFPVPilotResponse(FPVPilot pilot) {
        return FPVPilotSignUpResponse.builder()
                .id(pilot.getFpvPilotId())
                .firstname(pilot.getFirstname())
                .lastname(pilot.getLastname())
                .username(pilot.getUsername())
                .authorities(pilot.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                .createdAt(pilot.getCreatedAt())
                .updatedAt(pilot.getUpdatedAt())
                .build();
    }

    public FPVPilot mapToCreateFPVPilotEntity(FPVPilotAdminRequest fpvPilotAdminRequest) {
        return FPVPilot.builder()
                .username(fpvPilotAdminRequest.getUsername())
                .password(fpvPilotAdminRequest.getPassword())
                .firstname(fpvPilotAdminRequest.getFirstname())
                .lastname(fpvPilotAdminRequest.getLastname())
                .authorities(fpvPilotAdminRequest.getAuthorities())
                .build();
    }
}
