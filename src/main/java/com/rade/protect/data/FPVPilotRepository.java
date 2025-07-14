package com.rade.protect.data;

import com.rade.protect.model.entity.FPVPilot;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FPVPilotRepository extends JpaRepository<FPVPilot, Long> {

    @Override
    @NonNull
    Optional<FPVPilot> findById(Long id);

    Optional<FPVPilot> findByUsername(String username);

    Optional<FPVPilot> findByFirstname(String firstname);

    @Override
    @NonNull
    List<FPVPilot> findAll();

}
