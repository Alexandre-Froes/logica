package br.edu.iftm.unidade4.exercicio2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoServiceTest {

    @Test
    public void shouldCacheProductsAndReportStats() {
        ProdutoService service = new ProdutoService();

        assertNull(service.buscarPorId(999L));

        Produto p1 = service.buscarPorId(1L);
        assertNotNull(p1);
        assertEquals(1L, p1.getId());

        Produto p1Again = service.buscarPorId(1L);
        assertNotNull(p1Again);
        assertEquals(p1, p1Again);

        CacheStats stats = service.obterEstatisticas();
        assertEquals(1, stats.getEntradas());
        assertEquals(1, stats.getHits());
        assertEquals(2, stats.getMisses());
        assertTrue(stats.getTaxaAcerto() >= 0.0);
    }
}
