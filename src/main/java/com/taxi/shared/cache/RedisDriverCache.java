package com.taxi.shared.cache;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class RedisDriverCache {

    private final StringRedisTemplate redisTemplate;
    private static final String FREE_DRIVERS_KEY = "taxi:drivers:free";

    public RedisDriverCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addFreeDriver(Long driverId) {
        redisTemplate.opsForSet().add(FREE_DRIVERS_KEY, String.valueOf(driverId));
    }

    public void removeDriver(Long driverId) {
        redisTemplate.opsForSet().remove(FREE_DRIVERS_KEY, String.valueOf(driverId));
    }

    public String getRandomFreeDriverId() {
        return redisTemplate.opsForSet().randomMember(FREE_DRIVERS_KEY);
    }

    public void clear() {
        redisTemplate.delete(FREE_DRIVERS_KEY);
    }
}