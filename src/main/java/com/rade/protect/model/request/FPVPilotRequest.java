package com.rade.protect.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Valid
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FPVPilotRequest {

    @NotBlank(message = "FPV pilot name is required!")
    private String name;

    @NotBlank(message = "FPV pilot lastname is required!")
    private String lastName;

}