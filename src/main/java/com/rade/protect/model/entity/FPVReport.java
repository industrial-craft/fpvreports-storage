package com.rade.protect.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "Fpv_Report")
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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fpvPilot_id", referencedColumnName = "fpvPilotId", nullable = false)
    private FPVPilot fpvPilot;

    @Column(name = "isLostFPVDueToREB")
    private boolean isLostFPVDueToREB;

    @Column(name = "isOnTargetFPV")
    private boolean isOnTargetFPV;

    @Column(name = "coordinatesMGRS")
    private String coordinatesMGRS;

    @Column(name = "additionalInfo")
    private String additionalInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FPVReport fpvReport)) return false;
        if (this.fpvReportId == null || fpvReport.fpvReportId == null) return false;
        return this.fpvReportId.equals(fpvReport.fpvReportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fpvReportId);
    }

}
