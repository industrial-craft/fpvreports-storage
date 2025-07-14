package com.rade.protect.service;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenBlackListService {

    void addToBlacklist(HttpServletRequest httpServletRequest);

    Boolean isBlacklisted(String token);
}
