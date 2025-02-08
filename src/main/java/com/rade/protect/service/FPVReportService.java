package com.rade.protect.service;

import com.rade.protect.model.request.FPVReportRequest;
import com.rade.protect.model.response.FPVReportResponse;

import java.util.List;
import java.util.Optional;

public interface FPVReportService {

    Optional<FPVReportResponse> findById(Long id);

    boolean existsById(Long id);

    List<FPVReportResponse> findAll();

    FPVReportResponse save(FPVReportRequest fpvReportRequest);

    FPVReportResponse update(Long id, FPVReportRequest updatedReportRequest);

    void deleteById(Long id);

    void deleteAllByIds(List<Long> ids);

}
