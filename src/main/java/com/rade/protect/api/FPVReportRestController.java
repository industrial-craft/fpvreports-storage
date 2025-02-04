package com.rade.protect.api;

import com.rade.protect.api.validation.fpvserialnumber.UniqueFpvSerialNumber;
import com.rade.protect.model.request.FPVReport;
import com.rade.protect.model.request.FPVReportIds;
import com.rade.protect.model.response.FPVReportResponse;
import com.rade.protect.service.FPVReportRestService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/api/v1/fpvreports")
public class FPVReportRestController {

    @Autowired
    private FPVReportRestService fpvReportRestService;

    @GetMapping("/{id}")
    public ResponseEntity<FPVReport> findByIdFpvReport(@PathVariable Long id) {
        FPVReport fpvReport = fpvReportRestService.findById(id);
        return ResponseEntity.ok(fpvReport);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<FPVReport> fpvReports = fpvReportRestService.findAll();
        if (fpvReports.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new AppError(HttpStatus.OK.value(), "No FPVReports are available!"));
        }
        return ResponseEntity.ok(fpvReports);
    }

    @PostMapping
    public ResponseEntity<FPVReport> createFpvReport(@Valid @RequestBody @UniqueFpvSerialNumber FPVReport fpvReport) {
        log.info("Received FPV Report: {}", fpvReport);

        FPVReport savedFpvReport = fpvReportRestService.save(fpvReport);

        log.info("Saved FPV Report: {}", savedFpvReport);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(savedFpvReport)
                .toUri();

        return ResponseEntity.created(location).body(savedFpvReport);
    }

    private FPVReportResponse mapToResponse(FPVReport fpvReport) {
        return FPVReportResponse.builder()
                .fpvReportId(fpvReport.getFpvReportId())  // Переконайтеся, що ID тут є
                .fpvDrone(fpvReport.getFpvDrone())
                .dateTimeFlight(fpvReport.getDateTimeFlight())
                .fpvPilot(fpvReport.getFpvPilot())
                .isLostFPVDueToREB(fpvReport.getIsLostFPVDueToREB())
                .isOnTargetFPV(fpvReport.getIsOnTargetFPV())
                .coordinatesMGRS(fpvReport.getCoordinatesMGRS())
                .additionalInfo(fpvReport.getAdditionalInfo())
                .build();
    }

    @PutMapping("/{id}")
    ResponseEntity<FPVReport> updateFPVReport(@Valid @RequestBody FPVReport fpvReport, @PathVariable Long id) {
        FPVReport updateFPVReport = fpvReportRestService.updateFPVReport(id, fpvReport);
        return ResponseEntity.ok(updateFPVReport);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFPVReport(@PathVariable Long id) {
        fpvReportRestService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new AppError(HttpStatus.OK.value(), "FPVReports with ID: " + id + " are successfully deleted!"));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFPVReports(@Valid @RequestBody FPVReportIds fpvReportIds) {
        List<Long> ids = fpvReportIds.getFpvReportIds();
        fpvReportRestService.deleteAllById(ids);
        return ResponseEntity.status(HttpStatus.OK).body(new AppError(HttpStatus.OK.value(), "FPVReports with IDs: " + ids + " are successfully deleted!"));
    }

}
