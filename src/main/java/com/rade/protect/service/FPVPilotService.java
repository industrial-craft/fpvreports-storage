package com.rade.protect.service;

import com.rade.protect.model.request.FPVPilotAdminRequest;
import com.rade.protect.model.response.FPVPilotSignUpResponse;

import java.util.List;
import java.util.Optional;

public interface FPVPilotService {

    Optional<FPVPilotSignUpResponse> findById(Long id);

    List<FPVPilotSignUpResponse> findAll();

    FPVPilotSignUpResponse save(FPVPilotAdminRequest fpvPilotRequest);

    void deleteById(Long id);

}
