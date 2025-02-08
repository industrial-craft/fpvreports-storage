package com.rade.protect.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "FPV_Pilot")
public class FPVPilot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "fpvPilotId")
    private Long fpvPilotId;

    @Column(name = "name")
    private String name;

    @Column(name = "lastName")
    private String lastName;

    @OneToOne(mappedBy = "fpvPilot")
    @JsonIgnore
    private FPVReport fpvReport;

}
