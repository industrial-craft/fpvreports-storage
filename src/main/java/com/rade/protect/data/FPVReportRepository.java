package com.rade.protect.data;

import com.rade.protect.model.request.FPVDrone;
import com.rade.protect.model.request.FPVReport;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FPVReportRepository extends CrudRepository<FPVReport, Long> {

    List<FPVReport> findAll(Sort sort);

    List<FPVReport> findAllByFpvDrone_FpvModel(FPVDrone.FPVModel fpvModel);

    List<FPVReport> findAllByIsLostFPVDueToREB(Boolean isLostFPVDueToREB);

    List<FPVReport> findAllByIsOnTargetFPV(Boolean isOnTarget);

    List<FPVReport> findAllByFpvDrone_FpvSerialNumber(String serialNumber);

}
