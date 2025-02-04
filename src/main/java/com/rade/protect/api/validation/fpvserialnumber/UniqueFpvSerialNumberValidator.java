package com.rade.protect.api.validation.fpvserialnumber;

import com.rade.protect.data.FPVReportRepository;
import com.rade.protect.model.request.FPVReport;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueFpvSerialNumberValidator implements ConstraintValidator<UniqueFpvSerialNumber, FPVReport> {

    private final FPVReportRepository fpvReportRepository;

    @Override
    public boolean isValid(FPVReport fpvReport, ConstraintValidatorContext context) {
        if (fpvReport == null) return true;
        return fpvReportRepository.findAllByFpvDrone_FpvSerialNumber(fpvReport.getFpvDrone().getFpvSerialNumber()).isEmpty();
    }
}
