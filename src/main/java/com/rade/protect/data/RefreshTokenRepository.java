package com.rade.protect.data;

import com.rade.protect.model.entity.FPVPilot;
import com.rade.protect.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByFpvPilot(FPVPilot fpvPilot);

    void deleteByFpvPilot(FPVPilot fpvPilot);
}
