package com.rade.protect.service.impl;

import com.rade.protect.model.entity.FPVDrone;
import com.rade.protect.model.request.FPVDroneRequest;
import com.rade.protect.model.response.FPVDroneResponse;
import com.rade.protect.service.FPVDroneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FPVDroneServiceImpl implements FPVDroneService {

    public FPVDrone mapToFPVDroneEntity(FPVDroneRequest request) {
        return FPVDrone.builder()
                .fpvSerialNumber(request.getFpvSerialNumber())
                .fpvCraftName(request.getFpvCraftName())
                .fpvModel(request.getFpvModel())
                .build();
    }

    public FPVDroneResponse mapToFPVDroneResponse(FPVDrone fpvDrone) {
        return FPVDroneResponse.builder()
                .fpvSerialNumber(fpvDrone.getFpvSerialNumber())
                .fpvCraftName(fpvDrone.getFpvCraftName())
                .fpvModel(fpvDrone.getFpvModel())
                .build();
    }
}
