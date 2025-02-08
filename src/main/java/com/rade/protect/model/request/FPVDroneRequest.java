package com.rade.protect.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rade.protect.api.validation.fpvmodel.EnumNamePattern;
import com.rade.protect.api.validation.fpvmodel.FPVModelDeserializer;
import com.rade.protect.api.validation.fpvmodel.FPVModelSerializer;
import com.rade.protect.api.validation.fpvserialnumber.UniqueFpvSerialNumber;
import com.rade.protect.model.entity.FPVDrone;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FPVDroneRequest {

    @NotBlank(message = "FPV Serial Number is required!")
    private String fpvSerialNumber;

    @NotBlank(message = "FPV Craft Name is required!")
    private String fpvCraftName;

    @NotNull(message = "FPV Model is required!")
    @EnumNamePattern(regexp = "KAMIKAZE|BOMBER|PPO")
    private FPVDrone.FPVModel fpvModel;

    @JsonSerialize(using = FPVModelSerializer.class)
    @JsonDeserialize(using = FPVModelDeserializer.class)
    public enum FPVModel {

        KAMIKAZE, BOMBER, PPO;
    }

}
