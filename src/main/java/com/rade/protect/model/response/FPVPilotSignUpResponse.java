package com.rade.protect.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FPVPilotSignUpResponse {

    private Long id;

    private String firstname;

    private String lastname;

    private String username;

    private Set<String> authorities;

    private Date createdAt;

    private Date updatedAt;

}
