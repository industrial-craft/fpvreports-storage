package com.rade.protect.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "FPV_Report")
public class FPVReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fpvReportId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fpvDrone_id", referencedColumnName = "fpvDroneId")
    private FPVDrone fpvDrone;

    @Column(name = "dateTimeFlight")
    private LocalDateTime dateTimeFlight;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fpvPilot_id", referencedColumnName = "fpvPilotId")
    private FPVPilot fpvPilot;

    @Column(name = "isLostFPVDueToREB")
    private boolean isLostFPVDueToREB;

    @Column(name = "isOnTargetFPV")
    private boolean isOnTargetFPV;

    @Column(name = "coordinatesMGRS")
    private String coordinatesMGRS;

    @Column(name = "additionalInfo")
    private String additionalInfo;

}
