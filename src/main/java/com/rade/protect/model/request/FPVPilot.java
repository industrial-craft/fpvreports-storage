package com.rade.protect.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "FPV_Pilot")
public class FPVPilot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "fpvPilotId")
    private Long fpvPilotId;

    @NotBlank(message = "FPV pilot name is required!")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "FPV pilot lastname is required!")
    @Column(name = "lastName")
    private String lastName;

    @OneToOne(mappedBy = "fpvPilot")
    @JsonIgnore
    private FPVReport fpvReport;

}
