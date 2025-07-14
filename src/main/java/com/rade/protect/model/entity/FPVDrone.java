package com.rade.protect.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "Fpv_Drone")
public class FPVDrone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fpvDroneId")
    @JsonIgnore
    private Long fpvDroneId;

    @Column(name = "fpvSerialNumber")
    private String fpvSerialNumber;

    @Column(name = "fpvCraftName")
    private String fpvCraftName;

    @Enumerated(EnumType.STRING)
    @Column(name = "fpvModel")
    private FPVModel fpvModel;

    @OneToOne(mappedBy = "fpvDrone")
    @JsonIgnore
    private FPVReport fpvReport;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FPVDrone other)) return false;
        if (this.fpvDroneId == null || other.fpvDroneId == null) return false;
        return this.fpvDroneId.equals(other.fpvDroneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fpvDroneId);
    }

    public enum FPVModel {

        KAMIKAZE, BOMBER, PPO;
    }

}
