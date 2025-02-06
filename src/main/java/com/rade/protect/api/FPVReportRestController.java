package com.rade.protect.api;

import com.rade.protect.api.validation.exception.FPVReportNotFoundException;
import com.rade.protect.api.validation.fpvserialnumber.UniqueFpvSerialNumber;
import com.rade.protect.model.request.FPVReport;
import com.rade.protect.model.request.FPVReportIds;
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
    public ResponseEntity<?> findByIdFpvReport(@PathVariable Long id) {
/*        try {
            FPVReport fpvReport = fpvReportRestService.findById(id)
                    .orElseThrow(() -> new FPVReportNotFoundException("FPV Report with id - " + id + " is not found!"));
            return ResponseEntity.ok(fpvReport);
        } catch (FPVReportNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new AppError(HttpStatus.NOT_FOUND.value(), "FPV Report with id - " + id + " is not found!"));
        }*/
        FPVReport fpvReport = fpvReportRestService.findById(id)
                .orElseThrow(() -> new FPVReportNotFoundException("FPV Report with id - " + id + " is not found!"));
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

    @PutMapping("/{id}")
    ResponseEntity<?> updateFPVReport(@Valid @RequestBody FPVReport fpvReport, @PathVariable Long id) {
        FPVReport updatedFPVReport = fpvReportRestService.updateFPVReport(id, fpvReport);
        if (updatedFPVReport == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppError(HttpStatus.NOT_FOUND.value(), "FPV Report with id - " + id + " is not found!"));
        }
        return ResponseEntity.ok(updatedFPVReport);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFPVReport(@PathVariable Long id) {
        fpvReportRestService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new AppError(HttpStatus.OK.value(), "FPVReports with ID: " + id + " are successfully deleted!"));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFPVReports(@Valid @RequestBody FPVReportIds fpvReportIds) {
        List<Long> ids = fpvReportIds.getFpvReportIds();
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AppError(HttpStatus.BAD_REQUEST.value(), "No FPVReport IDs provided!"));
        }
        fpvReportRestService.deleteAllByIds(ids);
        return ResponseEntity.status(HttpStatus.OK).body(new AppError(HttpStatus.OK.value(), "FPVReports with IDs: " + ids + " are successfully deleted!"));
    }

}
