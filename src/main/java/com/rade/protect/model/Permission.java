package com.rade.protect.model;

import lombok.Getter;

@Getter
public enum Permission {
    FPVPILOT_READ("fpvpilot:read"),
    FPVPILOT_WRITE("fpvpilot:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

}
