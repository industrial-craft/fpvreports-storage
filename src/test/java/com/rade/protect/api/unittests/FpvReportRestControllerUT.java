package com.rade.protect.api.unittests;


import com.rade.protect.model.Role;
import com.rade.protect.model.entity.FPVDrone;
import com.rade.protect.model.entity.FPVPilot;
import com.rade.protect.model.entity.FPVReport;
import com.rade.protect.model.request.FPVDroneRequest;
import com.rade.protect.model.request.FPVPilotRequest;
import com.rade.protect.model.request.FPVPilotSignInRequest;
import com.rade.protect.model.request.FPVReportCreateRequest;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class FpvReportRestControllerUT {

    protected FPVPilotSignInRequest fpvPilotSignInRequest;

    protected FPVReportCreateRequest fpvReportCreateRequest;

    protected void givenValidFpvPilotSignInRequest() {
        fpvPilotSignInRequest = FPVPilotSignInRequest.builder()
                .username("fpvPilotUsername")
                .password("fpvPilotPassword")
                .build();
    }

    protected void giveValidFpvReportRequest() {
        fpvReportCreateRequest = FPVReportCreateRequest.builder()
                .fpvDrone(FPVDroneRequest.builder()
                        .fpvSerialNumber("SN743")
                        .fpvCraftName("Trainspotting")
                        .fpvModel(FPVDrone.FPVModel.KAMIKAZE)
                        .build())
                .dateTimeFlight(LocalDateTime.of(2025, 2, 3, 19, 0))
                .fpvPilot(FPVPilotRequest.builder()
                        .firstname("Dave")
                        .lastname("Clark")
                        .build())
                .isLostFPVDueToREB(false)
                .isOnTargetFPV(true)
                .coordinatesMGRS("37U DP 14256 82649")
                .additionalInfo("h: 243")
                .build();
    }

    protected FPVReport mapToEntity(FPVReportCreateRequest request, FPVPilot savedPilot) {
        FPVPilotRequest pilotRequest = request.getFpvPilot();
        savedPilot.setFirstname(pilotRequest.getFirstname());
        savedPilot.setLastname(pilotRequest.getLastname());

        return FPVReport.builder()
                .fpvDrone(mapToFPVDroneEntity(request.getFpvDrone()))
                .fpvPilot(savedPilot)
                .dateTimeFlight(request.getDateTimeFlight())
                .isLostFPVDueToREB(request.isLostFPVDueToREB())
                .isOnTargetFPV(request.isOnTargetFPV())
                .coordinatesMGRS(request.getCoordinatesMGRS())
                .additionalInfo(request.getAdditionalInfo())
                .build();
    }

    protected FPVDrone mapToFPVDroneEntity(FPVDroneRequest request) {
        return FPVDrone.builder()
                .fpvSerialNumber(request.getFpvSerialNumber())
                .fpvCraftName(request.getFpvCraftName())
                .fpvModel(request.getFpvModel())
                .build();
    }

    protected FPVPilot mapToSignInEntity(FPVPilotSignInRequest fpvPilotSignInRequest) {
        return FPVPilot.builder()
                .username(fpvPilotSignInRequest.getUsername())
                .password(fpvPilotSignInRequest.getPassword())
                .authorities(Role.USER.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                .build();
    }
}
