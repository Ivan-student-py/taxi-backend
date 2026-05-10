package com.taxi.shared.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisDriverCache {

    private final StringRedisTemplate redisTemplate;
    private static final String FREE_DRIVERS_KEY = "taxi:drivers:free";

    public RedisDriverCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addFreeDriver(Long driverId) {
        redisTemplate.opsForSet().add(FREE_DRIVERS_KEY, String.valueOf(driverId));
        log.info("Added driver #{} to Redis cache", driverId);
    }

    public void removeDriver(Long driverId) {
        redisTemplate.opsForSet().remove(FREE_DRIVERS_KEY, String.valueOf(driverId));
        log.info("Removed driver #{} from Redis cache", driverId);
    }

    public String getRandomFreeDriverId() {
        String driverId = redisTemplate.opsForSet().randomMember(FREE_DRIVERS_KEY);

        if (driverId != null) {
            log.info("CACHE HIT: Found free driver #{} in Redis (skipping DB query)", driverId);
        } else {
            log.info("CACHE MISS: Redis empty, falling back to database query");
        }

        return driverId;
    }

    public void clear() {
        redisTemplate.delete(FREE_DRIVERS_KEY);
        log.info("Cleared Redis driver cache");
    }
}