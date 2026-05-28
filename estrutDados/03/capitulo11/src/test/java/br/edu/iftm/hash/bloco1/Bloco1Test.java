package br.edu.iftm.hash.bloco1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes JUnit 5 para exercício 1.1
 */
@DisplayName("Exercício 1.1 - Contrato hashCode/equals")
class Bloco1ContratoTest {

    @Test
    @DisplayName("Produto: equals e hashCode devem ser consistentes")
    void testProdutoHashCodeEquals() {
        Produto p1 = new Produto("SKU-001", "Notebook");
        Produto p2 = new Produto("SKU-001", "Notebook");
        Produto p3 = new Produto("SKU-002", "Mouse");

        // equals: produtos com mesmo SKU são iguais
        assertEquals(p1, p2);

        // hashCode: se equals == true, hashCode deve ser igual
        assertEquals(p1.hashCode(), p2.hashCode());

        // produtos diferentes devem ter hashCode diferentes (na maioria dos casos)
        assertNotEquals(p1, p3);
    }

    @Test
    @DisplayName("Sessao: hashCode não pode ser constante")
    void testSessaoHashCode() {
        Sessao s1 = new Sessao("token-abc");
        Sessao s2 = new Sessao("token-xyz");

        // Cada sessão com token diferente deve ter hashCode diferente
        assertNotEquals(s1.hashCode(), s2.hashCode());

        // Mesmas chaves devem ter mesmo hashCode
        Sessao s1_copia = new Sessao("token-abc");
        assertEquals(s1.hashCode(), s1_copia.hashCode());
    }

    @Test
    @DisplayName("Coordenada: double deve comparar com precisão")
    void testCoordenadaEquals() {
        Coordenada c1 = new Coordenada(10.0, 20.0);
        Coordenada c2 = new Coordenada(10.0, 20.0);
        Coordenada c3 = new Coordenada(10.1, 20.0);

        // Mesmo valor devem ser iguais
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());

        // Valores diferentes devem ser diferentes
        assertNotEquals(c1, c3);
    }
}

/**
 * Testes JUnit 5 para exercício 1.3
 */
@DisplayName("Exercício 1.3 - TabelaHashEncadeada")
class TabelaHashEncadeadaTest {
    private TabelaHashEncadeada<String, Integer> tabela;

    @BeforeEach
    void setUp() {
        tabela = new TabelaHashEncadeada<>(4);
    }

    @Test
    @DisplayName("put e get: chave inserida deve ser recuperada")
    void testPutGet() {
        tabela.put("produto-42", 100);
        
        Integer valor = tabela.get("produto-42");
        assertNotNull(valor);
        assertEquals(100, valor);
    }

    @Test
    @DisplayName("put com atualização: tamanho não deve aumentar")
    void testAtualizacaoNaoIncrementaTamanho() {
        tabela.put("produto-42", 100);
        assertEquals(1, tabela.tamanho());

        // Atualiza o valor com a mesma chave
        tabela.put("produto-42", 150);
        assertEquals(1, tabela.tamanho());
        
        // Verifica que o valor foi atualizado
        assertEquals(150, tabela.get("produto-42"));
    }

    @Test
    @DisplayName("colisão: duas chaves no mesmo bucket devem ser recuperadas corretamente")
    void testColisao() {
        // Com capacidade 4, procuramos duas chaves que colidem no mesmo bucket
        // Vamos usar chaves que produzem o mesmo hashCode % 4
        // Exemplo: "key1".hashCode() % 4 == "key5".hashCode() % 4 não é garantido,
        // mas podemos usar chaves que sabemos que colidem

        // Inserir vários valores para forçar colisões no bucket 0
        tabela.put("a", 1);
        tabela.put("b", 2);
        tabela.put("c", 3);
        tabela.put("d", 4);

        // Verificar que todos foram armazenados
        assertEquals(4, tabela.tamanho());
        
        // Verificar que podemos recuperar todos mesmo se houver colisões
        assertEquals(1, tabela.get("a"));
        assertEquals(2, tabela.get("b"));
        assertEquals(3, tabela.get("c"));
        assertEquals(4, tabela.get("d"));
    }

    @Test
    @DisplayName("remove: deve remover e decrementar tamanho")
    void testRemove() {
        tabela.put("chave1", 10);
        tabela.put("chave2", 20);
        assertEquals(2, tabela.tamanho());

        boolean removido = tabela.remove("chave1");
        assertTrue(removido);
        assertEquals(1, tabela.tamanho());
        assertNull(tabela.get("chave1"));

        // Tentar remover chave inexistente
        boolean naoRemovido = tabela.remove("chave1");
        assertFalse(naoRemovido);
    }

    @Test
    @DisplayName("null: deve lançar exceção ao inserir null como chave")
    void testNullKey() {
        assertThrows(IllegalArgumentException.class, () -> {
            tabela.put(null, 100);
        });
    }
}
