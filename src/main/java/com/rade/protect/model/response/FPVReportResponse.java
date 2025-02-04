package com.rade.protect.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rade.protect.api.validation.localdatetime.LocalDateTimeCustomDeserializer;
import com.rade.protect.api.validation.localdatetime.LocalDateTimeCustomSerializer;
import com.rade.protect.model.request.FPVDrone;
import com.rade.protect.model.request.FPVPilot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FPVReportResponse {

    private Long fpvReportId;

    private FPVDrone fpvDrone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonSerialize(using = LocalDateTimeCustomSerializer.class)
    @JsonDeserialize(using = LocalDateTimeCustomDeserializer.class)
    private LocalDateTime dateTimeFlight;

    private FPVPilot fpvPilot;

    private Boolean isLostFPVDueToREB;

    private Boolean isOnTargetFPV;

    private String coordinatesMGRS;

    private String additionalInfo;

}
