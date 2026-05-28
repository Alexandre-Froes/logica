package br.edu.iftm.hash.bloco2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes JUnit 5 para Bloco 2
 */
@DisplayName("Bloco 2 - Endereçamento Aberto e Rehashing")
class TabelaHashAbertaTest {
    private TabelaHashAberta<String, Integer> tabela;

    @BeforeEach
    void setUp() {
        tabela = new TabelaHashAberta<>(10);
    }

    @Test
    @DisplayName("Rehashing é disparado quando λ >= 0.70")
    void testRehashingDisparo() {
        int capacidadeInicial = tabela.capacidade();
        
        // Inserir até atingir o limiar de 0.70
        // Com capacidade 10, isso seria 7 pares
        for (int i = 0; i < 7; i++) {
            tabela.put("chave-" + i, i);
        }

        // Fator de carga está em 0.70, próxima inserção deve disparar rehashing
        assertEquals(0.70, tabela.fatorDeCarga(), 0.01);
        
        int capacidadeAntes = tabela.capacidade();
        tabela.put("chave-7", 7);
        int capacidadeDepois = tabela.capacidade();

        // Capacidade deve ter dobrado
        assertEquals(capacidadeAntes * 2, capacidadeDepois);
    }

    @Test
    @DisplayName("Todos os pares são corretamente migrados durante rehashing")
    void testRehashingMigracao() {
        // Inserir vários pares
        for (int i = 0; i < 10; i++) {
            tabela.put("chave-" + i, i * 100);
        }

        // Após inserções múltiplas e rehashing, verifica que todos estão lá
        for (int i = 0; i < 10; i++) {
            assertEquals(i * 100, tabela.get("chave-" + i));
        }
    }

    @Test
    @DisplayName("put e get com endereçamento aberto")
    void testPutGet() {
        tabela.put("chave1", 42);
        assertEquals(42, tabela.get("chave1"));
        
        tabela.put("chave2", 100);
        assertEquals(100, tabela.get("chave2"));
        assertEquals(42, tabela.get("chave1")); // Verifica que chave1 não foi afetada
    }

    @Test
    @DisplayName("Atualização: put com chave existente não aumenta tamanho")
    void testAtualizacao() {
        tabela.put("chave", 10);
        assertEquals(1, tabela.tamanho());
        
        tabela.put("chave", 20);
        assertEquals(1, tabela.tamanho());
        assertEquals(20, tabela.get("chave"));
    }

    @Test
    @DisplayName("Remove com lazy deletion")
    void testRemove() {
        tabela.put("chave1", 10);
        tabela.put("chave2", 20);
        assertEquals(2, tabela.tamanho());

        boolean removido = tabela.remove("chave1");
        assertTrue(removido);
        assertEquals(1, tabela.tamanho());
        assertNull(tabela.get("chave1"));
        assertEquals(20, tabela.get("chave2"));
    }

    @Test
    @DisplayName("Sondagem linear: preenche slots vazios em ordem")
    void testSondagemLinear() {
        tabela = new TabelaHashAberta<>(5); // Pequena tabela
        
        // Estas inserções podem gerar colisões dependendo do hash
        tabela.put("a", 1);
        tabela.put("b", 2);
        tabela.put("c", 3);
        
        // Verifica recuperação mesmo com sondagem
        assertEquals(1, tabela.get("a"));
        assertEquals(2, tabela.get("b"));
        assertEquals(3, tabela.get("c"));
    }

    @Test
    @DisplayName("Fator de carga é calculado corretamente")
    void testFatorDeCarga() {
        assertEquals(0.0, tabela.fatorDeCarga());
        
        tabela.put("chave1", 10);
        assertEquals(1.0 / 10, tabela.fatorDeCarga(), 0.01);
        
        tabela.put("chave2", 20);
        assertEquals(2.0 / 10, tabela.fatorDeCarga(), 0.01);
    }
}
