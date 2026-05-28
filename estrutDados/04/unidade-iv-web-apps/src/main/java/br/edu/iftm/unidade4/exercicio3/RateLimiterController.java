package br.edu.iftm.unidade4.exercicio3;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/limiter")
public class RateLimiterController {

    private final RedisRateLimiter redisRateLimiter;

    public RateLimiterController(RedisRateLimiter redisRateLimiter) {
        this.redisRateLimiter = redisRateLimiter;
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<String> verificarLimite(@PathVariable String clienteId) {
        boolean permitido = redisRateLimiter.permitir(clienteId);
        if (permitido) {
            return ResponseEntity.ok("Permitido");
        }
        return ResponseEntity.status(429).body("Limite excedido");
    }
}
