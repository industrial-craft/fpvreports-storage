package com.rade.protect;

import com.rade.protect.model.request.FPVReport;
import lombok.Data;

import java.util.List;

@Data
public class FPVReportStorage {

    private List<FPVReport> fpvReports;

    public void addReport(FPVReport fpvReport) {
        fpvReports.add(fpvReport);
    }
}
