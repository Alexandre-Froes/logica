package br.edu.iftm.unidade4.exercicio2;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class LRUCacheTest {

    @Test
    public void shouldEvictLeastRecentlyUsed() {
        LRUCache<String, Produto> cache = new LRUCache<>(3);
        cache.put("p1", new Produto(1L, "Teclado", "T01", BigDecimal.valueOf(120.0), 18));
        cache.put("p2", new Produto(2L, "Mouse", "M02", BigDecimal.valueOf(45.5), 27));
        cache.put("p3", new Produto(3L, "Monitor", "MN03", BigDecimal.valueOf(850.0), 8));

        assertEquals(3, cache.size());
        cache.get("p1");
        cache.put("p4", new Produto(4L, "Notebook", "NB04", BigDecimal.valueOf(3200.0), 5));

        assertFalse(cache.containsKey("p2"));
        assertTrue(cache.containsKey("p1"));
        assertTrue(cache.containsKey("p3"));
        assertTrue(cache.containsKey("p4"));
    }
}
