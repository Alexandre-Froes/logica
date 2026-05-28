package br.edu.iftm.unidade4.exercicio3;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Component
public class RedisRateLimiter {
    private final StringRedisTemplate redisTemplate;
    private final int maxRequests;
    private final Duration windowDuration;

    public RedisRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.maxRequests = 100;
        this.windowDuration = Duration.ofMinutes(1);
    }

    public boolean permitir(String clienteId) {
        String chave = "rate-limiter:" + clienteId;
        Long contador = redisTemplate.opsForValue().increment(chave);
        if (Objects.equals(contador, 1L)) {
            redisTemplate.expire(chave, windowDuration);
        }
        return contador != null && contador <= maxRequests;
    }
}
