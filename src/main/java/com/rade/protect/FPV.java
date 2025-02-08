package com.rade.protect;

import com.rade.protect.model.entity.FPVDrone;
import lombok.Data;

import java.util.List;

@Data
public class FPV {

    private String name;

    private List<FPVDrone> fpvDrones;

}
