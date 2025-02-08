package com.rade.protect.service.impl;

import com.rade.protect.api.validation.exception.FPVReportNotFoundException;
import com.rade.protect.data.FPVReportRepository;
import com.rade.protect.model.entity.FPVReport;
import com.rade.protect.model.request.FPVReportRequest;
import com.rade.protect.model.response.FPVReportResponse;
import com.rade.protect.service.FPVReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FPVReportServiceImpl implements FPVReportService {

    @Autowired
    private FPVReportRepository fpvReportRepository;

    @Autowired
    private FPVDroneServiceImpl fpvDroneService;

    @Autowired
    private FPVPilotServiceImpl fpvPilotService;

    public Optional<FPVReportResponse> findById(Long id) {
        FPVReport fpvReport = fpvReportRepository.findById(id)
                .orElseThrow(() -> new FPVReportNotFoundException("FPV Report with id - " + id + " is not found!"));
        return Optional.ofNullable(mapToResponse(fpvReport));
    }

    public boolean existsById(Long id) {
        return true;
    }

    public List<FPVReportResponse> findAll() {
        List<FPVReport> fpvReports = new ArrayList<>();
        fpvReportRepository.findAll().forEach(fpvReports::add);
        return fpvReports.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public FPVReportResponse save(FPVReportRequest fpvReportRequest) {
        log.info("Saving FPVReport: {}", fpvReportRequest);
        FPVReport fpvReport = mapToEntity(fpvReportRequest);
        FPVReport savedFpvReport = fpvReportRepository.save(fpvReport);
        return mapToResponse(savedFpvReport);
    }

    public FPVReportResponse update(Long id, FPVReportRequest fpvReportRequest) {

        FPVReport existingFpvReport = fpvReportRepository.findById(id).orElseThrow(() -> new FPVReportNotFoundException("FPV Report with id - " + id + " is not found!"));

        existingFpvReport.setFpvDrone(fpvDroneService.mapToFPVDroneEntity(fpvReportRequest.getFpvDrone()));
        existingFpvReport.setFpvPilot(fpvPilotService.mapToFPVPilotEntity(fpvReportRequest.getFpvPilot()));
        existingFpvReport.setDateTimeFlight(fpvReportRequest.getDateTimeFlight());
        existingFpvReport.setIsLostFPVDueToREB(fpvReportRequest.getIsLostFPVDueToREB());
        existingFpvReport.setIsOnTargetFPV(fpvReportRequest.getIsOnTargetFPV());
        existingFpvReport.setCoordinatesMGRS(fpvReportRequest.getCoordinatesMGRS());
        existingFpvReport.setAdditionalInfo(fpvReportRequest.getAdditionalInfo());

        FPVReport updatedFpvReport = fpvReportRepository.save(existingFpvReport);
        return mapToResponse(updatedFpvReport);
    }

    public void deleteById(Long id) {
        FPVReport existingFpvReport = fpvReportRepository.findById(id)
                .orElseThrow(() -> new FPVReportNotFoundException("FPV Report with id - " + id + " is not found!"));
        fpvReportRepository.delete(existingFpvReport);

    }

    public void deleteAllByIds(List<Long> ids) {
        List<Long> filteredIds = ids.stream()
                .distinct()
                .toList();
        Iterable<FPVReport> existingFpvReports = fpvReportRepository.findAllById(filteredIds);
        List<Long> fpvReportIds = new ArrayList<>();
        for (FPVReport fpvReport : existingFpvReports) {
            fpvReportIds.add(fpvReport.getFpvReportId());
        }
        List<Long> missingIds = ids.stream()
                .filter(id -> !fpvReportIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new FPVReportNotFoundException("FPVReports not found for IDs: " + missingIds);
        }

        fpvReportRepository.deleteAllById(fpvReportIds);
    }

    private FPVReport mapToEntity(FPVReportRequest request) {
        return FPVReport.builder()
                .fpvDrone(fpvDroneService.mapToFPVDroneEntity(request.getFpvDrone()))
                .fpvPilot(fpvPilotService.mapToFPVPilotEntity(request.getFpvPilot()))
                .dateTimeFlight(request.getDateTimeFlight())
                .isLostFPVDueToREB(request.getIsLostFPVDueToREB())
                .isOnTargetFPV(request.getIsOnTargetFPV())
                .coordinatesMGRS(request.getCoordinatesMGRS())
                .additionalInfo(request.getAdditionalInfo())
                .build();
    }

    private FPVReportResponse mapToResponse(FPVReport fpvReport) {
        return FPVReportResponse.builder()
                .fpvReportId(fpvReport.getFpvReportId())
                .fpvDrone(fpvDroneService.mapToFPVDroneResponse(fpvReport.getFpvDrone()))
                .fpvPilot(fpvPilotService.mapToFPVDroneResponse(fpvReport.getFpvPilot()))
                .dateTimeFlight(fpvReport.getDateTimeFlight())
                .isLostFPVDueToREB(fpvReport.getIsLostFPVDueToREB())
                .isOnTargetFPV(fpvReport.getIsOnTargetFPV())
                .coordinatesMGRS(fpvReport.getCoordinatesMGRS())
                .additionalInfo(fpvReport.getAdditionalInfo())
                .build();
    }

}
