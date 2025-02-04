package com.rade.protect.data;

import com.rade.protect.model.request.FPVDrone;
import com.rade.protect.model.request.FPVPilot;
import com.rade.protect.model.request.FPVReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LoadDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    FPVReportRepository fpvReportRepository;

    @Override
    public void run(String... args)  {
        FPVDrone drone1 = new FPVDrone();
        drone1.setFpvSerialNumber("SN12345");
        drone1.setFpvCraftName("Drone Alpha");
        drone1.setFpvModel(FPVDrone.FPVModel.KAMIKAZE);

        FPVPilot pilot1 = new FPVPilot();
        pilot1.setName("John");
        pilot1.setLastName("Doe");

        FPVReport report1 = new FPVReport();
        report1.setFpvDrone(drone1);
        report1.setFpvPilot(pilot1);
        report1.setDateTimeFlight(LocalDateTime.of(2023, 11, 1, 14, 0));
        report1.setIsLostFPVDueToREB(false);
        report1.setIsOnTargetFPV(true);
        report1.setCoordinatesMGRS("38SMB448134");
        report1.setAdditionalInfo("Successful mission");

        log.info("Preloading {}", fpvReportRepository.save(report1));
    }
}
