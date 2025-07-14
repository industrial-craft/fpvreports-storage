package com.rade.protect.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rade.protect.api.validation.authorities.AuthoritiesDeserializer;
import com.rade.protect.api.validation.authorities.AuthoritiesSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FPVPilotAdminRequest {

    @NotBlank(message = "Username must not be null or empty!")
    private String username;

    @NotBlank(message = "Password must not be null or empty!")
    private String password;

    @NotBlank(message = "Firstname must not be null or empty!")
    private String firstname;

    @NotBlank(message = "Lastname must not be null or empty!")
    private String lastname;

    @NotEmpty(message = "Authorities must not be empty!")
    @JsonSerialize(using = AuthoritiesSerializer.class)
    @JsonDeserialize(using = AuthoritiesDeserializer.class)
    private Set<String> authorities;

}
