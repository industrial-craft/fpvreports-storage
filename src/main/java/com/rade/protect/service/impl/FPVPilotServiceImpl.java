package com.rade.protect.service.impl;

import com.rade.protect.model.entity.FPVPilot;
import com.rade.protect.model.request.FPVPilotRequest;
import com.rade.protect.model.response.FPVPilotResponse;
import com.rade.protect.service.FPVPilotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FPVPilotServiceImpl implements FPVPilotService {

    public FPVPilot mapToFPVPilotEntity(FPVPilotRequest request) {
        return FPVPilot.builder()
                .name(request.getName())
                .lastName(request.getLastName())
                .build();
    }

    public FPVPilotResponse mapToFPVDroneResponse(FPVPilot fpvPilot) {
        return FPVPilotResponse.builder()
                .name(fpvPilot.getName())
                .lastName(fpvPilot.getLastName())
                .build();
    }

}
