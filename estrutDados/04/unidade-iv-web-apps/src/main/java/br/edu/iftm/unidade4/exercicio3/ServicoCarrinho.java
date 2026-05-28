package br.edu.iftm.unidade4.exercicio3;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ServicoCarrinho {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CHAVE_BASE = "carrinho:";

    public ServicoCarrinho(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void adicionarItem(String usuarioId, String produtoId, int quantidade) {
        String chave = CHAVE_BASE + usuarioId;
        Map<String, Integer> carrinho = (Map<String, Integer>) redisTemplate.opsForValue().get(chave);
        if (carrinho == null) {
            carrinho = new HashMap<>();
        }
        carrinho.merge(produtoId, quantidade, Integer::sum);
        redisTemplate.opsForValue().set(chave, carrinho);
    }

    public Map<String, Integer> buscarCarrinho(String usuarioId) {
        String chave = CHAVE_BASE + usuarioId;
        Map<String, Integer> carrinho = (Map<String, Integer>) redisTemplate.opsForValue().get(chave);
        return carrinho == null ? new HashMap<>() : carrinho;
    }

    public void limparCarrinho(String usuarioId) {
        String chave = CHAVE_BASE + usuarioId;
        redisTemplate.delete(chave);
    }
}
