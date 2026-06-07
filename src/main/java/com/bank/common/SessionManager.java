package com.bank.common;

import com.bank.entity.SysUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class SessionManager {

    private static final String SESSION_PREFIX = "bank:session:";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${session.expire:1800}")
    private long expireTime;

    public String createSession(SysUser user) {
        String token = UUID.randomUUID().toString().replace("-", "");
        String key = SESSION_PREFIX + token;
        try {
            user.setTradePassword(null);
            String userJson = objectMapper.writeValueAsString(user);
            redisTemplate.opsForValue().set(key, userJson, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("创建会话失败", e);
        }
        return token;
    }

    public SysUser getSession(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        String key = SESSION_PREFIX + token;
        String userJson = redisTemplate.opsForValue().get(key);
        if (userJson == null) {
            return null;
        }
        try {
            return objectMapper.readValue(userJson, SysUser.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void refreshSession(String token) {
        if (token == null || token.isEmpty()) {
            return;
        }
        String key = SESSION_PREFIX + token;
        Boolean hasKey = redisTemplate.hasKey(key);
        if (Boolean.TRUE.equals(hasKey)) {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        }
    }

    public void removeSession(String token) {
        if (token == null || token.isEmpty()) {
            return;
        }
        String key = SESSION_PREFIX + token;
        redisTemplate.delete(key);
    }
}
