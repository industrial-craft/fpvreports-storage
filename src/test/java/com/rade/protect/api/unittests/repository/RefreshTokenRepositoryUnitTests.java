package com.rade.protect.api.unittests.repository;

import com.rade.protect.api.unittests.FpvPilotRestControllerUT;
import com.rade.protect.data.FPVPilotRepository;
import com.rade.protect.data.RefreshTokenRepository;
import com.rade.protect.model.entity.FPVPilot;
import com.rade.protect.model.entity.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RefreshTokenRepositoryUnitTests extends FpvPilotRestControllerUT {

    @Autowired
    private FPVPilotRepository fpvPilotRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private FPVPilot savedFpvPilot;
    private RefreshToken savedRefreshToken;

    @BeforeAll
    public void setup() {
        refreshTokenRepository.deleteAll();
        fpvPilotRepository.deleteAll();

        givenValidFpvPilotAdminRequest();
        FPVPilot fpvPilot = mapToCreateFPVPilotEntity(fpvPilotAdminRequest);
        savedFpvPilot = fpvPilotRepository.save(fpvPilot);

        RefreshToken refreshToken = RefreshToken.builder()
                .fpvPilot(savedFpvPilot)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        savedRefreshToken = refreshTokenRepository.save(refreshToken);

        log.info("BeforeAll — FPVPilot: {}", savedFpvPilot);
        log.info("BeforeAll — RefreshToken: {}", savedRefreshToken);
    }

    @Test
    @DisplayName("Test 1: Find refresh token by fpv pilot admin test")
    @Order(1)
    @Rollback(value = false)
    public void findRefreshTokenByFpvPilotAdminTest() {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByFpvPilot(savedFpvPilot);
        Assertions.assertThat(existingToken).isPresent();
        Assertions.assertThat(savedFpvPilot.getUsername()).isEqualTo(existingToken.get().getFpvPilot().getUsername());
    }

    @Test
    @DisplayName("Test 2: Find refresh token by token test")
    @Order(2)
    @Rollback(value = false)
    public void findRefreshTokenByTokenTest() {
        Optional<RefreshToken> actualToken = refreshTokenRepository.findByToken(savedRefreshToken.getToken());
        Assertions.assertThat(actualToken).isPresent();
        Assertions.assertThat(actualToken.get().getFpvPilot().getUsername()).isEqualTo(savedRefreshToken.getFpvPilot().getUsername());
    }

}
