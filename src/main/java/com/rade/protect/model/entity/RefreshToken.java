package com.rade.protect.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "Refresh_Token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "fpv_pilot_id", referencedColumnName = "fpvPilotId", nullable = false)
    private FPVPilot fpvPilot;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshToken refreshToken)) return false;
        if (this.id == null || refreshToken.id == null) return false;

        return Objects.equals(this.id, refreshToken.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
