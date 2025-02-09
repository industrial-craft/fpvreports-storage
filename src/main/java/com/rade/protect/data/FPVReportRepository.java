package com.rade.protect.data;

import com.rade.protect.model.entity.FPVDrone;
import com.rade.protect.model.entity.FPVReport;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FPVReportRepository extends JpaRepository<FPVReport, Long> {

    @Override
    @NonNull
    List<FPVReport> findAll();

    List<FPVReport> findAllByFpvDrone_FpvModel(FPVDrone.FPVModel fpvModel);

    List<FPVReport> findAllByIsLostFPVDueToREB(Boolean isLostFPVDueToREB);

    List<FPVReport> findAllByIsOnTargetFPV(Boolean isOnTarget);

    List<FPVReport> findAllByFpvDrone_FpvSerialNumber(String serialNumber);

}
