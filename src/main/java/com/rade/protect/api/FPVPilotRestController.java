package com.rade.protect.api;

import com.rade.protect.api.validation.exception.FPVPilotNotFoundException;
import com.rade.protect.model.entity.FPVPilot;
import com.rade.protect.model.request.FPVPilotAdminRequest;
import com.rade.protect.model.request.FPVPilotRequest;
import com.rade.protect.model.request.FPVPilotSignUpRequest;
import com.rade.protect.model.response.FPVPilotResponse;
import com.rade.protect.model.response.FPVPilotSignUpResponse;
import com.rade.protect.service.impl.FPVPilotServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/fpvpilots")
public class FPVPilotRestController {

    @Autowired
    private FPVPilotServiceImpl fpvPilotServiceImpl;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('fpvpilot:write')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        FPVPilotSignUpResponse fpvPilotResponse = fpvPilotServiceImpl.findById(id)
                .orElseThrow(() -> new FPVPilotNotFoundException("FPV pilot with id - " + id + " is not found!"));
        return ResponseEntity.ok(fpvPilotResponse);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('fpvpilot:write')")
    public ResponseEntity<?> findAll() {
        List<FPVPilotSignUpResponse> fpvPilotsResponse = fpvPilotServiceImpl.findAll();
        if (fpvPilotsResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new AppError(HttpStatus.OK.value(), "No FPV pilots are available!"));
        }
        return ResponseEntity.of(Optional.of(fpvPilotsResponse));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('fpvpilot:write')")
    public ResponseEntity<?> save(@Valid @RequestBody FPVPilotAdminRequest fpvPilotRequest) {
        log.info("Inside save fpv pilot");
        FPVPilotSignUpResponse fpvPilotResponse = fpvPilotServiceImpl.save(fpvPilotRequest);
        log.info("Fpv pilot response: {}", fpvPilotResponse.toString());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand()
                .toUri();

        return ResponseEntity.created(location).body(fpvPilotResponse);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('fpvpilot:write')")
    public ResponseEntity<?> update(@Valid @PathVariable Long id, @RequestBody FPVPilotAdminRequest fpvPilotRequest) {
        if (!fpvPilotServiceImpl.existsById(id)) {
            throw new FPVPilotNotFoundException("FPV pilot with id - " + id + " is not found!");
        }
        FPVPilotSignUpResponse fpvPilotResponse = fpvPilotServiceImpl.update(id, fpvPilotRequest);
        return ResponseEntity.ok(fpvPilotResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('fpvpilot:write')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            fpvPilotServiceImpl.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new AppError(HttpStatus.OK.value(), "FPV pilot with ID: " + id + " is successfully deleted!"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new AppError(HttpStatus.FORBIDDEN.value(), e.getMessage()));
        } catch (FPVPilotNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        }
    }



}
