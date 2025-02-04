package com.rade.protect.service;

import com.rade.protect.data.FPVDroneRepository;
import com.rade.protect.data.FPVPilotRepository;
import com.rade.protect.data.FPVReportRepository;
import com.rade.protect.model.request.FPVDrone;
import com.rade.protect.model.request.FPVPilot;
import com.rade.protect.model.request.FPVReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FPVReportService {

    @Autowired
    private FPVReportRepository fpvReportRepository;

    @Autowired
    private FPVDroneRepository fpvDroneRepository;

    @Autowired
    private FPVPilotRepository fpvPilotRepository;

    public List<FPVReport> findAll() {
        List<FPVReport> fpvReports = new ArrayList<>();
        fpvReportRepository.findAll().forEach(fpvReports::add);
        return fpvReports;
    }

    public Optional<FPVReport> findById(Long id) {
        return fpvReportRepository.findById(id);
    }

    public List<FPVReport> findAllByFpvDrone_SerialNumber(String serialNumber) {
        return fpvReportRepository.findAllByFpvDrone_FpvSerialNumber(serialNumber);
    }

    public boolean existsById(Long id) {
        return true;
    }

    public FPVReport save(FPVReport fpvReport) {

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

    public void deleteById(Long id) {
        fpvReportRepository.deleteById(id);
    }

    public void deleteAllById(List<Long> ids) {
        fpvReportRepository.deleteAllById(ids);
    }

    public FPVReport updateFPVReport(Long id, FPVReport updatedReport) {
        FPVReport existingReport = fpvReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid FPVReport ID: " + id));

        FPVDrone existingDrone = fpvDroneRepository.findById(existingReport.getFpvDrone().getFpvDroneId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid FPVDrone ID: " + existingReport.getFpvDrone().getFpvDroneId()));

        FPVPilot existingPilot = fpvPilotRepository.findById(existingReport.getFpvPilot().getFpvPilotId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid FPVPilot ID: " + existingReport.getFpvPilot().getFpvPilotId()));

        existingDrone.setFpvSerialNumber(updatedReport.getFpvDrone().getFpvSerialNumber());
        existingDrone.setFpvCraftName(updatedReport.getFpvDrone().getFpvCraftName());
        existingDrone.setFpvModel(updatedReport.getFpvDrone().getFpvModel());

        existingPilot.setName(updatedReport.getFpvPilot().getName());
        existingPilot.setLastName(updatedReport.getFpvPilot().getLastName());

        existingReport.setFpvDrone(existingDrone);
        existingReport.setFpvPilot(existingPilot);
        existingReport.setDateTimeFlight(updatedReport.getDateTimeFlight());
        existingReport.setIsLostFPVDueToREB(updatedReport.getIsLostFPVDueToREB());
        existingReport.setIsOnTargetFPV(updatedReport.getIsOnTargetFPV());
        existingReport.setCoordinatesMGRS(updatedReport.getCoordinatesMGRS());
        existingReport.setAdditionalInfo(updatedReport.getAdditionalInfo());

        return fpvReportRepository.save(existingReport);
    }

    public List<FPVReport> findAllSortedByDate() {
        return fpvReportRepository.findAll(Sort.by(Sort.Direction.DESC, "dateTimeFlight"));
    }

    public List<FPVReport> findAllSortedByPilotSurName() {
        return fpvReportRepository.findAll(Sort.by(Sort.Direction.ASC, "fpvPilot.surName"));
    }

    public List<FPVReport> findAllSortedByPilotName() {
        return fpvReportRepository.findAll(Sort.by(Sort.Direction.ASC, "fpvPilot.name"));
    }


}
