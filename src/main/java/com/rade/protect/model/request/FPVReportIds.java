package com.rade.protect.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rade.protect.api.validation.fpvreportids.FPVReportIdsDeserializer;
import lombok.Data;

import java.util.List;

@Data
public class FPVReportIds {

    @JsonDeserialize(using = FPVReportIdsDeserializer.class)
    private List<Long> fpvReportIds;
}
