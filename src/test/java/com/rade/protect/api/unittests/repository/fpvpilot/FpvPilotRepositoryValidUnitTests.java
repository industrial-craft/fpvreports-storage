package com.rade.protect.api.unittests.repository.fpvpilot;

import com.rade.protect.api.unittests.FpvPilotRestControllerUT;
import com.rade.protect.data.FPVPilotRepository;
import com.rade.protect.model.entity.FPVPilot;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@Slf4j
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FpvPilotRepositoryValidUnitTests extends FpvPilotRestControllerUT {

    @Autowired
    private FPVPilotRepository fpvPilotRepository;

    @Test
    @DisplayName("Test 1: Save fpv pilot admin test")
    @Order(1)
    @Rollback(value = false)
    public void saveFpvPilotAdminTest() {
        givenValidFpvPilotAdminRequest();
        FPVPilot fpvPilot = mapToCreateFPVPilotEntity(fpvPilotAdminRequest);
        FPVPilot savedFpvPilot = fpvPilotRepository.save(fpvPilot);
        Assertions.assertThat(savedFpvPilot.getFpvPilotId()).isNotNull();
        Assertions.assertThat(savedFpvPilot.getFirstname()).isEqualTo("Elliot");
    }

    @Test
    @DisplayName("Test 2: Save fpv pilot user test")
    @Order(2)
    @Rollback(value = false)
    public void saveFpvPilotUserTest() {
        givenValidFpvPilotUserRequest();
        FPVPilot fpvPilot = mapToCreateFPVPilotEntity(fpvPilotUserRequest);
        FPVPilot savedFpvPilot = fpvPilotRepository.save(fpvPilot);
        Assertions.assertThat(savedFpvPilot.getFpvPilotId()).isNotNull();
        Assertions.assertThat(savedFpvPilot.getFirstname()).isEqualTo("Darlene");
    }

    @Test
    @DisplayName("Test 3: Get first fpv pilot admin test")
    @Order(3)
    @Rollback(value = false)
    public void getFpvPilotUserByIdTest() {
        Optional<FPVPilot> fpvPilot = fpvPilotRepository.findById(1L);
        fpvPilot.ifPresent(pilot -> Assertions.assertThat(pilot.getFpvPilotId()).isEqualTo(1L));
    }

    @Test
    @DisplayName("Test 4: Get all fpv pilots test")
    @Order(4)
    @Rollback(value = false)
    public void getFpvPilotUserTest() {
        List<FPVPilot> fpvPilots = fpvPilotRepository.findAll();
        Assertions.assertThat(fpvPilots.size()).isGreaterThan(0);
    }

}
