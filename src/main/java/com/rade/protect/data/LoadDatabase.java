package com.rade.protect.data;

import com.rade.protect.model.entity.FPVDrone;
import com.rade.protect.model.entity.FPVPilot;
import com.rade.protect.model.entity.FPVReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LoadDatabase implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Autowired
    FPVReportRepository fpvReportRepository;

    @Autowired
    FPVPilotRepository fpvPilotRepository;

    //PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args)  {

        /*FPVDrone drone1 = new FPVDrone();
        drone1.setFpvSerialNumber("SN12345");
        drone1.setFpvCraftName("Drone Alpha");
        drone1.setFpvModel(FPVDrone.FPVModel.KAMIKAZE);

        FPVPilot pilot1 = FPVPilot.builder()
                .username("pilot1")
                .password(passwordEncoder.encode("pilot1"))
                .firstname("John")
                .lastname("Doe")
                .build();

        pilot1 = fpvPilotRepository.save(pilot1);

        FPVDrone drone2 = new FPVDrone();
        drone2.setFpvSerialNumber("SN123454");
        drone2.setFpvCraftName("Drone Beta");
        drone2.setFpvModel(FPVDrone.FPVModel.BOMBER);

        FPVPilot pilot2 = FPVPilot.builder()
                .username("pilot2")
                .password(passwordEncoder.encode("pilot2"))
                .firstname("Jeff")
                .lastname("Bezos")
                .build();

        pilot2 = fpvPilotRepository.save(pilot2);

        FPVReport report1 = new FPVReport();
        report1.setFpvDrone(drone1);
        report1.setFpvPilot(pilot1);
        report1.setDateTimeFlight(LocalDateTime.of(2023, 11, 1, 14, 0));
        report1.setLostFPVDueToREB(false);
        report1.setOnTargetFPV(true);
        report1.setCoordinatesMGRS("38SMB448134");
        report1.setAdditionalInfo("Successful mission");

        FPVReport report2 = new FPVReport();
        report2.setFpvDrone(drone2);
        report2.setFpvPilot(pilot2);
        report2.setDateTimeFlight(LocalDateTime.of(2025, 2, 20, 20, 0));
        report2.setLostFPVDueToREB(false);
        report2.setOnTargetFPV(true);
        report2.setCoordinatesMGRS("38SMB448242");
        report2.setAdditionalInfo("Successful mission");

        log.info("Preloading {}", fpvReportRepository.save(report1));
        log.info("Preloading {}", fpvReportRepository.save(report2));*/
    }
}
