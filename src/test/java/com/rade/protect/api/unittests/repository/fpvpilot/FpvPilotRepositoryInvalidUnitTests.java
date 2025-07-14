package com.rade.protect.api.unittests.repository.fpvpilot;

import com.rade.protect.api.unittests.FpvPilotRestControllerUT;
import com.rade.protect.data.FPVPilotRepository;
import com.rade.protect.model.entity.FPVPilot;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@Slf4j
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FpvPilotRepositoryInvalidUnitTests extends FpvPilotRestControllerUT {

    @Autowired
    private FPVPilotRepository fpvPilotRepository;

    @Test
    @DisplayName("Test 1: Save fpv pilot admin when username is null test")
    @Order(1)
    public void saveFpvPilotAdminWhenUsernameIsNullTest() {
        givenValidFpvPilotAdminRequest();
        fpvPilotAdminRequest.setUsername(null);
        FPVPilot invalidFpvPilot = mapToCreateFPVPilotEntity(fpvPilotAdminRequest);
        Assertions.assertThatThrownBy(() -> fpvPilotRepository.saveAndFlush(invalidFpvPilot))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
