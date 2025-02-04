package com.rade.protect.web;

import com.rade.protect.data.FPVDroneRepository;
import com.rade.protect.model.request.FPVDrone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FPVDroneByIdConverter implements Converter<Long, FPVDrone> {

    private FPVDroneRepository fpvDroneRepository;

    @Autowired
    public FPVDroneByIdConverter(FPVDroneRepository fpvDroneRepository) {
        this.fpvDroneRepository = fpvDroneRepository;
    }

    @Override
    public FPVDrone convert(Long fpvDroneId) {
        return fpvDroneRepository.findById(fpvDroneId).orElse(null);
    }

}
