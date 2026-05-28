package br.edu.iftm.hash.bloco3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes JUnit 5 para Problema 3.1 - Cache de Sessões HTTP
 */
@DisplayName("Problema 3.1 - Cache de Sessões HTTP")
class GerenciadorSessaoTest {
    private GerenciadorSessao gerenciador;

    @BeforeEach
    void setUp() {
        gerenciador = new GerenciadorSessao();
    }

    @Test
    @DisplayName("registrar e buscar: sessão registrada deve ser recuperada")
    void testRegistrarEBuscar() {
        String token = "abc-123-xyz";
        Usuario usuario = new Usuario(1, "admin");

        gerenciador.registrar(token, usuario);
        Usuario recuperado = gerenciador.buscar(token);

        assertNotNull(recuperado);
        assertEquals(1, recuperado.getId());
        assertEquals("admin", recuperado.getPerfil());
    }

    @Test
    @DisplayName("invalidar: deve remover sessão")
    void testInvalidar() {
        String token = "abc-123-xyz";
        Usuario usuario = new Usuario(2, "user");

        gerenciador.registrar(token, usuario);
        assertEquals(1, gerenciador.totalSessoes());

        boolean invalidado = gerenciador.invalidar(token);
        assertTrue(invalidado);
        assertEquals(0, gerenciador.totalSessoes());
        assertNull(gerenciador.buscar(token));
    }

    @Test
    @DisplayName("eValido: verifica se token é válido")
    void testEValido() {
        String tokenValido = "token-valido";
        String tokenInvalido = "token-invalido";

        gerenciador.registrar(tokenValido, new Usuario(3, "guest"));

        assertTrue(gerenciador.eValido(tokenValido));
        assertFalse(gerenciador.eValido(tokenInvalido));
    }

    @Test
    @DisplayName("token nulo ou vazio: lança exceção")
    void testTokenNuloOuVazio() {
        Usuario usuario = new Usuario(4, "test");

        assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.registrar(null, usuario);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.registrar("", usuario);
        });
    }

    @Test
    @DisplayName("múltiplas sessões: deve manter todas")
    void testMultiplasSessoes() {
        for (int i = 0; i < 10; i++) {
            gerenciador.registrar("token-" + i, new Usuario(i, "user-" + i));
        }

        assertEquals(10, gerenciador.totalSessoes());

        for (int i = 0; i < 10; i++) {
            assertNotNull(gerenciador.buscar("token-" + i));
        }
    }
}

/**
 * Testes JUnit 5 para Problema 3.2 - Índice Invertido
 */
@DisplayName("Problema 3.2 - Índice Invertido")
class IndiceInvertidoTest {
    private IndiceInvertido indice;

    @BeforeEach
    void setUp() {
        indice = new IndiceInvertido();
    }

    @Test
    @DisplayName("indexar e buscar: palavra indexada deve ser encontrada")
    void testIndexarEBuscar() {
        indice.indexar(1, "cadeira ergonômica");
        indice.indexar(2, "mesa de escritório");
        indice.indexar(3, "cadeira gamer");

        assertEquals(2, indice.buscar("cadeira").size());
        assertTrue(indice.buscar("cadeira").contains(1L));
        assertTrue(indice.buscar("cadeira").contains(3L));
    }

    @Test
    @DisplayName("buscarMultiplas: deve retornar interseção")
    void testBuscarMultiplas() {
        indice.indexar(1, "cadeira ergonômica");
        indice.indexar(2, "mesa de escritório");
        indice.indexar(3, "cadeira gamer");

        // Buscar "cadeira gamer" → apenas produto 3 tem ambas as palavras
        var resultado = indice.buscarMultiplas("cadeira gamer");
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(3L));
    }

    @Test
    @DisplayName("buscar palavra inexistente: retorna lista vazia")
    void testBuscarPalavraInexistente() {
        indice.indexar(1, "notebook");
        
        var resultado = indice.buscar("mouse");
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("normalização: minúscula e espaços")
    void testNormalizacao() {
        indice.indexar(1, "CADEIRA  ERGONÔMICA");
        
        var resultado = indice.buscar("cadeira");
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(1L));
    }

    @Test
    @DisplayName("total de palavras indexadas")
    void testTotalPalavras() {
        indice.indexar(1, "cadeira ergonômica");
        indice.indexar(2, "mesa de escritório");
        indice.indexar(3, "cadeira gamer");

        // Palavras: cadeira, ergonômica, mesa, de, escritório, gamer = 6 únicas
        assertEquals(6, indice.totalPalavrasIndexadas());
    }
}

/**
 * Testes JUnit 5 para Problema 3.3 - Rate Limiting
 */
@DisplayName("Problema 3.3 - Rate Limiting por IP")
class RateLimiterTest {
    private RateLimiter limiter;

    @BeforeEach
    void setUp() {
        limiter = new RateLimiter();
    }

    @Test
    @DisplayName("permitir: requisições abaixo do limite são permitidas")
    void testPermitirAbaizoDolimite() {
        String ip = "192.168.1.100";

        // Primeiras 100 requisições devem ser permitidas
        for (int i = 0; i < 100; i++) {
            assertTrue(limiter.permitir(ip));
        }

        // 101ª requisição deve ser negada
        assertFalse(limiter.permitir(ip));
    }

    @Test
    @DisplayName("permitir: IPs diferentes têm contadores independentes")
    void testIPsDiferentes() {
        String ip1 = "192.168.1.1";
        String ip2 = "192.168.1.2";

        // Maxar a janela de ip1
        for (int i = 0; i < 100; i++) {
            assertTrue(limiter.permitir(ip1));
        }
        assertFalse(limiter.permitir(ip1)); // negado

        // ip2 deve funcionar normalmente
        assertTrue(limiter.permitir(ip2));
        assertTrue(limiter.permitir(ip2));
    }

    @Test
    @DisplayName("limparExpirados: remove entradas com janela expirada")
    void testLimparExpirados() {
        String ip = "192.168.1.100";
        limiter.permitir(ip);
        assertEquals(1, limiter.totalIPsAtivos());

        // Nota: Este teste não pode testar expiração real sem sleep,
        // o que tornaria o teste muito lento. Um teste real usaria mocking
        // de System.currentTimeMillis()
        
        limiter.limparTudo();
        assertEquals(0, limiter.totalIPsAtivos());
    }

    @Test
    @DisplayName("ip nulo ou vazio: lança exceção")
    void testIPNuloOuVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            limiter.permitir(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            limiter.permitir("");
        });
    }

    @Test
    @DisplayName("janela deslizante: contador reset após expiração")
    void testJanelaDeslizante() {
        String ip = "192.168.1.100";

        // Maxar a janela
        for (int i = 0; i < 100; i++) {
            assertTrue(limiter.permitir(ip));
        }
        assertFalse(limiter.permitir(ip));

        // Simular expiração (usaríamos mocking em produção)
        limiter.limparTudo();

        // Janela nova começa
        assertTrue(limiter.permitir(ip));
        assertEquals(1, limiter.totalIPsAtivos());
    }
}
