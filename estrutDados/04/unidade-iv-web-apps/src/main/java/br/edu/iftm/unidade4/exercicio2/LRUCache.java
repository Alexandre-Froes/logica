package br.edu.iftm.unidade4.exercicio2;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Exercício 2: Cache LRU simples usando LinkedHashMap
 */
public class LRUCache<K, V> {
    private final int capacidade;
    private final LinkedHashMap<K, V> cache;

    public LRUCache(int capacidade) {
        this.capacidade = capacidade;
        this.cache = new LinkedHashMap<>(capacidade, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRUCache.this.capacidade;
            }
        };
    }

    public V put(K chave, V valor) {
        return cache.put(chave, valor);
    }

    public V get(K chave) {
        return cache.get(chave);
    }

    public int size() {
        return cache.size();
    }

    public boolean containsKey(K chave) {
        return cache.containsKey(chave);
    }

    @Override
    public String toString() {
        return cache.toString();
    }
}
