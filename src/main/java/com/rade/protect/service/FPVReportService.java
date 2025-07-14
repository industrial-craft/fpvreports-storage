package com.rade.protect.service;

import com.rade.protect.model.request.FPVReportCreateRequest;
import com.rade.protect.model.request.FPVReportUpdateRequest;
import com.rade.protect.model.response.FPVReportResponse;

import java.util.List;
import java.util.Optional;

public interface FPVReportService {

    Optional<FPVReportResponse> findById(Long id);

    boolean existsById(Long id);

    List<FPVReportResponse> findAll();

    FPVReportResponse save(FPVReportCreateRequest fpvReportCreateRequest);

    FPVReportResponse update(Long id, FPVReportUpdateRequest fpvReportUpdateRequest);

    void deleteById(Long id);

    void deleteAllByIds(List<Long> ids);

}
