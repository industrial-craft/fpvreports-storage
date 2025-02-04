package com.rade.protect.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AppError {

    private int statusCode;
    private String message;

}
