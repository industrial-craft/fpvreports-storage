package com.rade.protect.model.request;

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

    @NotBlank(message = "FPV Serial Number is required!")
    @Column(name = "fpvSerialNumber")
    private String fpvSerialNumber;

    @NotBlank(message = "FPV Craft Name is required!")
    @Column(name = "fpvCraftName")
    private String fpvCraftName;

    @NotNull(message = "FPV Model is required!")
    @EnumNamePattern(regexp = "KAMIKAZE|BOMBER|PPO")
    @Enumerated(EnumType.STRING)
    @Column(name = "fpvModel")
    private FPVModel fpvModel;

    @OneToOne(mappedBy = "fpvDrone")
    @JsonIgnore
    private FPVReport fpvReport;

    @JsonSerialize(using = FPVModelSerializer.class)
    @JsonDeserialize(using = FPVModelDeserializer.class)
    public enum FPVModel {

        KAMIKAZE, BOMBER, PPO;
    }

}
