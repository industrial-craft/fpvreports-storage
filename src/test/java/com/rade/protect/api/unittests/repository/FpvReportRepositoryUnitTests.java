package com.rade.protect.api.unittests.repository;

import com.rade.protect.api.unittests.FpvReportRestControllerUT;
import com.rade.protect.data.FPVPilotRepository;
import com.rade.protect.data.FPVReportRepository;
import com.rade.protect.model.entity.FPVPilot;
import com.rade.protect.model.entity.FPVReport;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FpvReportRepositoryUnitTests extends FpvReportRestControllerUT {

    @Autowired
    private FPVReportRepository fpvReportRepository;

    @Autowired
    private FPVPilotRepository fpvPilotRepository;

    @Test
    @DisplayName("Test 1: Save fpv report Test")
    @Order(1)
    @Rollback(value = false)
    public void saveFpvReportTest() {
        givenValidFpvPilotSignInRequest();
        FPVPilot fpvPilot = mapToSignInEntity(fpvPilotSignInRequest);
        FPVPilot savedFpvPilot = fpvPilotRepository.save(fpvPilot);
        giveValidFpvReportRequest();

        FPVReport savedFpvReport = mapToEntity(fpvReportCreateRequest, savedFpvPilot);
        fpvReportRepository.save(savedFpvReport);

        assertNotNull(savedFpvReport.getFpvReportId());
        Optional<FPVReport> foundReport = fpvReportRepository.findById(savedFpvReport.getFpvReportId());
        assertTrue(foundReport.isPresent());
        assertEquals("37U DP 14256 82649", foundReport.get().getCoordinatesMGRS());
        assertEquals(savedFpvPilot.getFpvPilotId(), foundReport.get().getFpvPilot().getFpvPilotId());
    }

    @Test
    @DisplayName("Test 2: Get fpv report Test")
    @Order(2)
    @Rollback(value = false)
    public void getFpvReportTest() {
        Optional<FPVReport> fpvReport = fpvReportRepository.findById(1L);
        fpvReport.ifPresent(report -> Assertions.assertThat(report.getFpvReportId()).isEqualTo(1L));
    }

    @Test
    @DisplayName("Test 3: Get list of fpv reports Test")
    @Order(3)
    @Rollback(value = false)
    public void getListOfFpvReportsTest() {
        List<FPVReport> fpvReports = fpvReportRepository.findAll();
        Assertions.assertThat(fpvReports.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Test 4: Update fpv report Test")
    @Order(4)
    @Rollback(value = false)
    public void updateFpvReportTest() {
        FPVReport fpvReport = fpvReportRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Fpv report with ID 1 not found"));
        fpvReport.setAdditionalInfo("h: 153");
        FPVReport updatedFpvReport = fpvReportRepository.save(fpvReport);
        Assertions.assertThat(updatedFpvReport.getAdditionalInfo()).isEqualTo("h: 153");
    }

    @Test
    @DisplayName("Test 5: Delete fpv report Test")
    @Order(5)
    @Rollback(value = false)
    public void deleteFpvReportTest() {
        fpvReportRepository.deleteById(1L);
        Optional<FPVReport> fpvReport = fpvReportRepository.findById(1L);
        Assertions.assertThat(fpvReport).isEmpty();
    }


}
