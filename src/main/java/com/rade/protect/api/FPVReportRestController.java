package com.rade.protect.api;

import com.rade.protect.api.validation.exception.FPVReportNotFoundException;
import com.rade.protect.api.validation.fpvserialnumber.UniqueFpvSerialNumber;
import com.rade.protect.model.request.FPVReportIds;
import com.rade.protect.model.request.FPVReportCreateRequest;
import com.rade.protect.model.request.FPVReportUpdateRequest;
import com.rade.protect.model.response.FPVReportResponse;
import com.rade.protect.service.impl.FPVReportServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private FPVReportServiceImpl fpvReportServiceImpl;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('fpvpilot:read')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        FPVReportResponse fpvReportResponse = fpvReportServiceImpl.findById(id)
                .orElseThrow(() -> new FPVReportNotFoundException("FPV Report with id - " + id + " is not found!"));
        return ResponseEntity.ok(fpvReportResponse);

    }

    @GetMapping
    @PreAuthorize("hasAuthority('fpvpilot:read')")
    public ResponseEntity<?> findAll() {
        List<FPVReportResponse> fpvReportsResponse = fpvReportServiceImpl.findAll();
        if (fpvReportsResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new AppError(HttpStatus.OK.value(), "No FPVReports are available!"));
        }
        return ResponseEntity.ok(fpvReportsResponse);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('fpvpilot:read')")
    public ResponseEntity<FPVReportResponse> save(@Valid @RequestBody @UniqueFpvSerialNumber FPVReportCreateRequest fpvReportCreateRequest) {
        FPVReportResponse fpvReportResponse = fpvReportServiceImpl.save(fpvReportCreateRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand()
                .toUri();

        return ResponseEntity.created(location).body(fpvReportResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('fpvpilot:read')")
    ResponseEntity<?> update(@Valid @RequestBody FPVReportUpdateRequest fpvReportUpdateRequest, @PathVariable Long id) {
        if (!fpvReportServiceImpl.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppError(HttpStatus.NOT_FOUND.value(), "FPV Report with id - " + id + " is not found!"));
        }
        FPVReportResponse updatedFPVReportResponse = fpvReportServiceImpl.update(id, fpvReportUpdateRequest);
        return ResponseEntity.ok(updatedFPVReportResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('fpvpilot:read')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        fpvReportServiceImpl.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new AppError(HttpStatus.OK.value(), "FPVReport with ID: " + id + " is successfully deleted!"));
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('fpvpilot:read')")
    public ResponseEntity<?> deleteAllByIds(@Valid @RequestBody FPVReportIds fpvReportIds) {
        List<Long> ids = fpvReportIds.getFpvReportIds();
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AppError(HttpStatus.BAD_REQUEST.value(), "No FPVReport IDs provided!"));
        }
        fpvReportServiceImpl.deleteAllByIds(ids);
        return ResponseEntity.status(HttpStatus.OK).body(new AppError(HttpStatus.OK.value(), "FPVReports with IDs: " + ids + " are successfully deleted!"));
    }

}
