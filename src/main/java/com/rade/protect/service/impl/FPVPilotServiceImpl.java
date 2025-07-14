package com.rade.protect.service.impl;

import com.rade.protect.api.validation.exception.FPVPilotNotFoundException;
import com.rade.protect.api.validation.exception.UsernameIsAlreadyExistsException;
import com.rade.protect.data.FPVPilotRepository;
import com.rade.protect.model.Role;
import com.rade.protect.model.entity.FPVPilot;
import com.rade.protect.model.request.FPVPilotAdminRequest;
import com.rade.protect.model.request.FPVPilotRequest;
import com.rade.protect.model.request.FPVPilotSignUpRequest;
import com.rade.protect.model.response.FPVPilotResponse;
import com.rade.protect.model.response.FPVPilotSignUpResponse;
import com.rade.protect.service.FPVPilotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FPVPilotServiceImpl implements FPVPilotService {

    private final FPVPilotRepository fpvPilotRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public FPVPilotSignUpResponse register(FPVPilotSignUpRequest request) {
        if (fpvPilotRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameIsAlreadyExistsException("Username - " + request.getUsername() + " is already exists!");
        }
        FPVPilot fpvPilot = mapToSignUpEntity(request);
        FPVPilot savedFpvPilot = fpvPilotRepository.save(fpvPilot);
        return mapToCreateFPVPilotResponse(savedFpvPilot);
    }

    @Transactional(readOnly = true)
    public FPVPilot findByUsername(String username) {
        return fpvPilotRepository.findByUsername(username)
                .orElseThrow(() -> new FPVPilotNotFoundException("FPV Pilot with username - " + username + " is not found!"));
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return fpvPilotRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Optional<FPVPilotSignUpResponse> findById(Long id) {
        return fpvPilotRepository.findById(id).map(this::mapToCreateFPVPilotResponse);
    }

    @Transactional(readOnly = true)
    public List<FPVPilotSignUpResponse> findAll() {
        List<FPVPilot> fpvPilots = fpvPilotRepository.findAll();
        return fpvPilots.stream()
                .map(this::mapToCreateFPVPilotResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FPVPilotSignUpResponse save(FPVPilotAdminRequest fpvPilotRequest) {
        if (fpvPilotRepository.findByUsername(fpvPilotRequest.getUsername()).isPresent()) {
            throw new UsernameIsAlreadyExistsException("Username - " + fpvPilotRequest.getUsername() + " is already exists!");
        }
        FPVPilot fpvPilot = mapToCreateFPVPilotEntity(fpvPilotRequest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        FPVPilot currentUser = (FPVPilot) authentication.getPrincipal();

        fpvPilot.setCreatedBy(currentUser);

        FPVPilot savedFpvPilot = fpvPilotRepository.save(fpvPilot);
        return mapToCreateFPVPilotResponse(savedFpvPilot);
    }

    @Transactional
    public FPVPilotSignUpResponse update(Long id, FPVPilotAdminRequest fpvPilotRequest) {
        FPVPilot existingFpvPilot = fpvPilotRepository.findById(id)
                .orElseThrow(() -> new FPVPilotNotFoundException("FPV Pilot with id - " + id + " is not found!"));

        existingFpvPilot.setUsername(fpvPilotRequest.getUsername());
        existingFpvPilot.setPassword(fpvPilotRequest.getPassword());
        existingFpvPilot.setFirstname(fpvPilotRequest.getFirstname());
        existingFpvPilot.setLastname(fpvPilotRequest.getLastname());
        existingFpvPilot.setAuthorities(fpvPilotRequest.getAuthorities());

        FPVPilot updatedFpvPilot = fpvPilotRepository.save(existingFpvPilot);
        return mapToCreateFPVPilotResponse(updatedFpvPilot);
    }

    @Transactional
    public void deleteById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        FPVPilot currentUser = (FPVPilot) authentication.getPrincipal();

        if (currentUser.getFpvPilotId().equals(id)) {
            throw new IllegalStateException("You can't delete yourself!");
        }

        FPVPilot existingFpvPilot = fpvPilotRepository.findById(id)
                .orElseThrow(() -> new FPVPilotNotFoundException("FPV Pilot with id - " + id + " is not found!"));

        if (existingFpvPilot.getCreatedBy() == null || !existingFpvPilot.getCreatedBy().getFpvPilotId().equals(currentUser.getFpvPilotId())) {
            throw new IllegalStateException("You can only delete users created by you!");
        }

        log.info("Deleting FPV pilot with id: {}", id);
        fpvPilotRepository.deleteById(existingFpvPilot.getFpvPilotId());
    }

    public FPVPilotSignUpResponse mapToCreateFPVPilotResponse(FPVPilot pilot) {
        return FPVPilotSignUpResponse.builder()
                .id(pilot.getFpvPilotId())
                .firstname(pilot.getFirstname())
                .lastname(pilot.getLastname())
                .username(pilot.getUsername())
                .authorities(pilot.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                .createdAt(pilot.getCreatedAt())
                .updatedAt(pilot.getUpdatedAt())
                .build();
    }

    public FPVPilot mapToCreateFPVPilotEntity(FPVPilotAdminRequest fpvPilotAdminRequest) {
        return FPVPilot.builder()
                .username(fpvPilotAdminRequest.getUsername())
                .password(passwordEncoder.encode(fpvPilotAdminRequest.getPassword()))
                .firstname(fpvPilotAdminRequest.getFirstname())
                .lastname(fpvPilotAdminRequest.getLastname())
                .authorities(fpvPilotAdminRequest.getAuthorities())
                .build();
    }

    public FPVPilot mapToFPVPilotEntity(FPVPilotRequest request) {
        return FPVPilot.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .build();
    }

    public FPVPilotResponse mapToFPVPilotResponse(FPVPilot fpvPilot) {
        return FPVPilotResponse.builder()
                .firstname(fpvPilot.getFirstname())
                .lastname(fpvPilot.getLastname())
                .build();
    }

    private FPVPilot mapToSignUpEntity(FPVPilotSignUpRequest request) {
        return FPVPilot.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .authorities(Role.USER.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                .build();
    }

}
