package br.edu.iftm.unidade4.exercicio2;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProdutoService {

    private final LRUCache<Long, Produto> cache;
    private final Map<Long, Produto> repositorio;
    private long hits;
    private long misses;

    public ProdutoService() {
        this.cache = new LRUCache<>(5);
        this.repositorio = new HashMap<>();
        this.hits = 0;
        this.misses = 0;
        inicializarProdutos();
    }

    private void inicializarProdutos() {
        repositorio.put(1L, new Produto(1L, "Teclado mecânico", "TECL-001", BigDecimal.valueOf(250.0), 20));
        repositorio.put(2L, new Produto(2L, "Mouse gamer", "MOUSE-002", BigDecimal.valueOf(99.90), 35));
        repositorio.put(3L, new Produto(3L, "Monitor 24", "MON-024", BigDecimal.valueOf(899.90), 12));
        repositorio.put(4L, new Produto(4L, "SSD 512GB", "SSD-512", BigDecimal.valueOf(439.90), 8));
        repositorio.put(5L, new Produto(5L, "Memória 16GB", "RAM-16", BigDecimal.valueOf(299.00), 15));
    }

    public Produto buscarPorId(Long id) {
        Produto produto = cache.get(id);
        if (produto != null) {
            hits++;
            return produto;
        }

        misses++;
        produto = repositorio.get(id);
        if (produto != null) {
            cache.put(id, produto);
        }
        return produto;
    }

    public CacheStats obterEstatisticas() {
        long total = hits + misses;
        double taxaAcerto = total == 0 ? 0.0 : (double) hits / total;
        return new CacheStats(cache.size(), hits, misses, taxaAcerto, 5);
    }
}
