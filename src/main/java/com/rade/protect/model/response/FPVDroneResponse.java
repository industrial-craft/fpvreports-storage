package com.rade.protect.model.response;

import com.rade.protect.model.entity.FPVDrone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FPVDroneResponse {

    private String fpvSerialNumber;

    private String fpvCraftName;
    
    private FPVDrone.FPVModel fpvModel;
}
