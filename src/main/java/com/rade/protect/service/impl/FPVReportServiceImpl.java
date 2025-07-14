package com.rade.protect.service.impl;

import com.rade.protect.api.validation.exception.FPVReportNotFoundException;
import com.rade.protect.data.FPVReportRepository;
import com.rade.protect.model.entity.FPVPilot;
import com.rade.protect.model.entity.FPVReport;
import com.rade.protect.model.request.FPVReportCreateRequest;
import com.rade.protect.model.request.FPVReportUpdateRequest;
import com.rade.protect.model.response.FPVReportResponse;
import com.rade.protect.service.FPVReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FPVReportServiceImpl implements FPVReportService {

    private final FPVReportRepository fpvReportRepository;

    private final FPVDroneServiceImpl fpvDroneService;

    private final FPVPilotServiceImpl fpvPilotService;

    @Transactional(readOnly = true)
    public Optional<FPVReportResponse> findById(Long id) {
        return fpvReportRepository.findById(id).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return fpvReportRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<FPVReportResponse> findAll() {
        List<FPVReport> fpvReports = fpvReportRepository.findAll();
        return fpvReports.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public FPVReportResponse save(FPVReportCreateRequest fpvReportCreateRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        FPVPilot existingPilot = fpvPilotService.findByUsername(username);
        FPVReport fpvReport = mapToEntity(fpvReportCreateRequest);
        fpvReport.setFpvPilot(existingPilot);
        FPVReport savedFpvReport = fpvReportRepository.save(fpvReport);
        return mapToResponse(savedFpvReport);
    }

    public FPVReportResponse update(Long id, FPVReportUpdateRequest fpvReportUpdateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated!");
        }
        String username = authentication.getName();
        FPVReport existingFpvReport = fpvReportRepository.findById(id)
                .orElseThrow(() -> new FPVReportNotFoundException("FPV Report with id - " + id + " is not found!"));

        if (!existingFpvReport.getFpvPilot().getUsername().equals(username)) {
            log.warn("User {} attempted to update FPV Report with id {}", username, id);
            throw new AccessDeniedException("You are not allowed to update this FPV Report!");
        }
        existingFpvReport.setFpvDrone(fpvDroneService.mapToFPVDroneEntity(fpvReportUpdateRequest.getFpvDrone()));
        existingFpvReport.setDateTimeFlight(fpvReportUpdateRequest.getDateTimeFlight());
        existingFpvReport.setLostFPVDueToREB(fpvReportUpdateRequest.isLostFPVDueToREB());
        existingFpvReport.setOnTargetFPV(fpvReportUpdateRequest.isOnTargetFPV());
        existingFpvReport.setCoordinatesMGRS(fpvReportUpdateRequest.getCoordinatesMGRS());
        existingFpvReport.setAdditionalInfo(fpvReportUpdateRequest.getAdditionalInfo());

        FPVReport updatedFpvReport = fpvReportRepository.save(existingFpvReport);
        return mapToResponse(updatedFpvReport);
    }

    public void deleteById(Long id) {
        FPVReport existingFpvReport = fpvReportRepository.findById(id)
                .orElseThrow(() -> new FPVReportNotFoundException("FPV Report with id - " + id + " is not found!"));
        log.info("Deleting FPV Report with id: {}", id);
        fpvReportRepository.delete(existingFpvReport);
    }

    public void deleteAllByIds(List<Long> ids) {
        List<FPVReport> existingFpvReports = fpvReportRepository.findAllById(ids);
        if (existingFpvReports.size() != ids.size()) {
            List<Long> foundIds = existingFpvReports.stream()
                    .map(FPVReport::getFpvReportId)
                    .toList();
            List<Long> missingIds = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new FPVReportNotFoundException("FPVReports not found for IDs: " + missingIds);
        }

        log.info("Deleting FPV Reports with ids: {}", ids);
        fpvReportRepository.deleteAllById(ids);
    }

    private FPVReport mapToEntity(FPVReportCreateRequest request) {
        return FPVReport.builder()
                .fpvDrone(fpvDroneService.mapToFPVDroneEntity(request.getFpvDrone()))
                .fpvPilot(fpvPilotService.mapToFPVPilotEntity(request.getFpvPilot()))
                .dateTimeFlight(request.getDateTimeFlight())
                .isLostFPVDueToREB(request.isLostFPVDueToREB())
                .isOnTargetFPV(request.isOnTargetFPV())
                .coordinatesMGRS(request.getCoordinatesMGRS())
                .additionalInfo(request.getAdditionalInfo())
                .build();
    }

    private FPVReportResponse mapToResponse(FPVReport fpvReport) {
        return FPVReportResponse.builder()
                .fpvReportId(fpvReport.getFpvReportId())
                .fpvDrone(fpvDroneService.mapToFPVDroneResponse(fpvReport.getFpvDrone()))
                .fpvPilot(fpvPilotService.mapToFPVPilotResponse(fpvReport.getFpvPilot()))
                .dateTimeFlight(fpvReport.getDateTimeFlight())
                .isLostFPVDueToREB(fpvReport.isLostFPVDueToREB())
                .isOnTargetFPV(fpvReport.isOnTargetFPV())
                .coordinatesMGRS(fpvReport.getCoordinatesMGRS())
                .additionalInfo(fpvReport.getAdditionalInfo())
                .build();
    }
}