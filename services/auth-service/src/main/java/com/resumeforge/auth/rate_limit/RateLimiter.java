package com.resumeforge.auth.rate_limit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

/**
 * Rate limiter component using Bucket4j.
 *
 * Provides IP-based rate limiting with configurable limits.
 */
@Component
public class RateLimiter {

    private final Cache<String, Bucket> cache = Caffeine.newBuilder()
            .expireAfterAccess(1, TimeUnit.HOURS)
            .maximumSize(100000)
            .build();

    private static final long REQUESTS_PER_MINUTE = 60;
    private static final long LOGIN_REQUESTS_PER_MINUTE = 5;
    private static final long REGISTER_REQUESTS_PER_MINUTE = 3;

    /**
     * Get or create bucket for IP address
     */
    public Bucket resolveBucket(String ip) {
        return cache.get(ip, key -> createNewBucket(REQUESTS_PER_MINUTE));
    }

    /**
     * Get or create bucket for login endpoint
     */
    public Bucket resolveLoginBucket(String ip) {
        String key = "login:" + ip;
        return cache.get(key, k -> createNewBucket(LOGIN_REQUESTS_PER_MINUTE));
    }

    /**
     * Get or create bucket for register endpoint
     */
    public Bucket resolveRegisterBucket(String ip) {
        String key = "register:" + ip;
        return cache.get(key, k -> createNewBucket(REGISTER_REQUESTS_PER_MINUTE));
    }

    /**
     * Create new bucket with specified capacity
     */
    private Bucket createNewBucket(long capacity) {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.intervally(capacity, Duration.ofMinutes(1)));
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }
}
