package com.rade.protect.service;

import com.rade.protect.api.validation.exception.FPVReportNotFoundException;
import com.rade.protect.data.FPVReportRepository;
import com.rade.protect.model.request.FPVDrone;
import com.rade.protect.model.request.FPVPilot;
import com.rade.protect.model.request.FPVReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FPVReportRestService {

    @Autowired
    private FPVReportRepository fpvReportRepository;

    public Optional<FPVReport> findById(Long id) {
        return Optional.ofNullable(fpvReportRepository.findById(id)
                .orElseThrow(() -> new FPVReportNotFoundException("FPV Report with id - " + id + " is not found!")));
    }

    public boolean existsById(Long id) {
        return true;
    }

    public List<FPVReport> findAll() {
        List<FPVReport> fpvReports = new ArrayList<>();
        fpvReportRepository.findAll().forEach(fpvReports::add);
        return fpvReports;
    }

    public FPVReport save(FPVReport fpvReport) {
        log.info("Saving fpvReport...");
        FPVDrone fpvDrone = FPVDrone.builder()
                .fpvSerialNumber(fpvReport.getFpvDrone().getFpvSerialNumber())
                .fpvCraftName(fpvReport.getFpvDrone().getFpvCraftName())
                .fpvModel(fpvReport.getFpvDrone().getFpvModel())
                .build();

        FPVPilot fpvPilot = FPVPilot.builder()
                .name(fpvReport.getFpvPilot().getName())
                .lastName(fpvReport.getFpvPilot().getLastName())
                .build();

        FPVReport newFpvReport = FPVReport.builder()
                .fpvDrone(fpvDrone)
                .fpvPilot(fpvPilot)
                .dateTimeFlight(fpvReport.getDateTimeFlight())
                .isLostFPVDueToREB(fpvReport.getIsLostFPVDueToREB())
                .isOnTargetFPV(fpvReport.getIsOnTargetFPV())
                .coordinatesMGRS(fpvReport.getCoordinatesMGRS())
                .additionalInfo(fpvReport.getAdditionalInfo())
                .build();

        return fpvReportRepository.save(newFpvReport);
    }

    public FPVReport updateFPVReport(Long id, FPVReport updatedReport) {

        FPVReport existingFpvReport = fpvReportRepository.findById(id).orElseThrow(() -> new FPVReportNotFoundException("FPV Report with id - " + id + " is not found!"));
        FPVDrone updatedFpvDrone = existingFpvReport.getFpvDrone();
        FPVPilot updatedFpvPilot = existingFpvReport.getFpvPilot();

        updatedFpvDrone.setFpvSerialNumber(updatedReport.getFpvDrone().getFpvSerialNumber());
        updatedFpvDrone.setFpvCraftName(updatedReport.getFpvDrone().getFpvCraftName());
        updatedFpvDrone.setFpvModel(updatedReport.getFpvDrone().getFpvModel());

        updatedFpvPilot.setName(updatedReport.getFpvPilot().getName());
        updatedFpvPilot.setLastName(updatedReport.getFpvPilot().getLastName());

        existingFpvReport.setFpvDrone(updatedFpvDrone);
        existingFpvReport.setFpvPilot(updatedFpvPilot);
        existingFpvReport.setDateTimeFlight(updatedReport.getDateTimeFlight());
        existingFpvReport.setIsLostFPVDueToREB(updatedReport.getIsLostFPVDueToREB());
        existingFpvReport.setIsOnTargetFPV(updatedReport.getIsOnTargetFPV());
        existingFpvReport.setCoordinatesMGRS(updatedReport.getCoordinatesMGRS());
        existingFpvReport.setAdditionalInfo(updatedReport.getAdditionalInfo());

        return fpvReportRepository.save(existingFpvReport);
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

}
