package com.rade.protect.api.validation.fpvserialnumber;

import com.rade.protect.data.FPVReportRepository;
import com.rade.protect.model.request.FPVReportRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueFpvSerialNumberValidator implements ConstraintValidator<UniqueFpvSerialNumber, FPVReportRequest> {

    private final FPVReportRepository fpvReportRepository;

    @Override
    public boolean isValid(FPVReportRequest fpvReport, ConstraintValidatorContext context) {
        if (fpvReport == null || fpvReport.getFpvDrone() == null || fpvReport.getFpvDrone().getFpvSerialNumber() == null) {
            return true;
        }
        return fpvReportRepository.findAllByFpvDrone_FpvSerialNumber(fpvReport.getFpvDrone().getFpvSerialNumber()).isEmpty();
    }
}
