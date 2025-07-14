package com.rade.protect.service.impl;

import com.rade.protect.service.TokenBlackListService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBlackListServiceImpl implements TokenBlackListService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JwtServiceImpl jwtService;

    @Override
    public void addToBlacklist(HttpServletRequest httpServletRequest) {
        String token = jwtService.extractTokenFromRequest(httpServletRequest);
        Date expiry = jwtService.extractExpiration(token);
        long expiration = expiry.getTime() - System.currentTimeMillis();
        redisTemplate.opsForValue().set(token, "blacklisted", expiration, TimeUnit.MILLISECONDS);
    }

    @Override
    public Boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }
}
