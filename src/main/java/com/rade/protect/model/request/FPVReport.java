package com.rade.protect.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rade.protect.api.validation.localdatetime.LocalDateTimeCustomDeserializer;
import com.rade.protect.api.validation.localdatetime.LocalDateTimeCustomSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "FPV_Report")
public class FPVReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fpvReportId;

    @NotNull(message = "FPVDrone object can't be null!")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fpvDrone_id", referencedColumnName = "fpvDroneId")
    private FPVDrone fpvDrone;

    @NotNull(message = "Date and time of the flight can't be null!")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonSerialize(using = LocalDateTimeCustomSerializer.class)
    @JsonDeserialize(using = LocalDateTimeCustomDeserializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "dateTimeFlight")
    private LocalDateTime dateTimeFlight;

    @NotNull(message = "FPVPilot object can't be null!")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fpvPilot_id", referencedColumnName = "fpvPilotId")
    private FPVPilot fpvPilot;

    @Column(name = "isLostFPVDueToREB")
    private Boolean isLostFPVDueToREB;

    @Column(name = "isOnTargetFPV")
    private Boolean isOnTargetFPV;

    @NotBlank(message = "Сoordinates MRGS are required!")
    @Column(name = "coordinatesMGRS")
    private String coordinatesMGRS;

    @NotBlank(message = "Additional info is required!")
    @Column(name = "additionalInfo")
    private String additionalInfo;

/*    @AssertTrue(message = "The field birthDate is invalid")
    private boolean isValidBirthDate() {
        return Optional.ofNullable(this.dateTimeFlight)
                .filter(date -> date.isAfter(LocalDateTime.of(2024, 1, 1, 1, 1)))
                .filter(date -> date.isBefore(LocalDateTime.now()))
                .isPresent();
    }*/

}
