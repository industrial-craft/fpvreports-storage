package com.rade.protect.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rade.protect.api.validation.localdatetime.LocalDateTimeCustomDeserializer;
import com.rade.protect.api.validation.localdatetime.LocalDateTimeCustomSerializer;
import com.rade.protect.model.entity.FPVDrone;
import com.rade.protect.model.entity.FPVPilot;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Valid
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FPVReportRequest {

    @NotNull(message = "FPVDrone object can't be null!")
    private FPVDroneRequest fpvDrone;

    @NotNull(message = "Date and time of the flight can't be null!")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonSerialize(using = LocalDateTimeCustomSerializer.class)
    @JsonDeserialize(using = LocalDateTimeCustomDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTimeFlight;

    @NotNull(message = "FPVPilot object can't be null!")
    private FPVPilotRequest fpvPilot;

    private Boolean isLostFPVDueToREB;

    private Boolean isOnTargetFPV;

    @NotBlank(message = "Сoordinates MRGS are required!")
    private String coordinatesMGRS;

    @NotBlank(message = "Additional info is required!")
    private String additionalInfo;
}

