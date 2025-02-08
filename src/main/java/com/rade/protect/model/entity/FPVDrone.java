package com.rade.protect.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rade.protect.api.validation.fpvmodel.EnumNamePattern;
import com.rade.protect.api.validation.fpvmodel.FPVModelDeserializer;
import com.rade.protect.api.validation.fpvmodel.FPVModelSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "FPV_Drone")
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

    public enum FPVModel {

        KAMIKAZE, BOMBER, PPO;
    }

}
