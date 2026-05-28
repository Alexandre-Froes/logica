package br.edu.iftm.hash.bloco2;

/**
 * Exercício 2.3: Tabela Hash com Endereçamento Aberto
 * Sondagem linear com rehashing automático
 */
public class TabelaHashAberta<K, V> {
    // Marcador de slot deletado (lazy deletion)
    private static final Object DELETADO = new Object();
    private static final double LIMIAR_REHASH = 0.70;

    private Object[] chaves;
    private Object[] valores;
    private int tamanho;
    private int capacidade;

    public TabelaHashAberta(int capacidade) {
        this.capacidade = capacidade;
        this.chaves = new Object[capacidade];
        this.valores = new Object[capacidade];
        this.tamanho = 0;
    }

    /**
     * Sondagem linear: h(k, i) = (h(k) + i) % capacidade
     */
    private int sonda(K chave, int i) {
        return (Math.abs(chave.hashCode()) + i) % capacidade;
    }

    /**
     * Insere ou atualiza par (chave, valor).
     * Dispara rehashing se fator de carga >= 0.70
     */
    @SuppressWarnings("unchecked")
    public void put(K chave, V valor) {
        // Verifica se é hora de fazer rehashing
        if (fatorDeCarga() >= LIMIAR_REHASH) {
            rehash();
        }

        for (int i = 0; i < capacidade; i++) {
            int idx = sonda(chave, i);
            if (chaves[idx] == null || chaves[idx] == DELETADO) {
                chaves[idx] = chave;
                valores[idx] = valor;
                tamanho++;
                return;
            }
            if (chaves[idx].equals(chave)) {
                valores[idx] = valor; // atualização
                return;
            }
        }
        throw new IllegalStateException("Tabela cheia");
    }

    /**
     * Recupera valor associado à chave, ou null se não encontrado
     */
    @SuppressWarnings("unchecked")
    public V get(K chave) {
        for (int i = 0; i < capacidade; i++) {
            int idx = sonda(chave, i);
            if (chaves[idx] == null) {
                return null; // Slot vazio, chave não existe
            }
            if (chaves[idx] != DELETADO && chaves[idx].equals(chave)) {
                return (V) valores[idx];
            }
        }
        return null;
    }

    /**
     * Remove a chave. Retorna true se existia, false caso contrário.
     * Usa lazy deletion (marca como DELETADO, não remove realmente)
     */
    public boolean remove(K chave) {
        for (int i = 0; i < capacidade; i++) {
            int idx = sonda(chave, i);
            if (chaves[idx] == null) {
                return false; // Slot vazio, chave não existe
            }
            if (chaves[idx] != DELETADO && chaves[idx].equals(chave)) {
                chaves[idx] = DELETADO;
                valores[idx] = null;
                tamanho--;
                return true;
            }
        }
        return false;
    }

    /**
     * Dobra a capacidade e reinserere todos os pares válidos
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        Object[] chavasAntigas = chaves;
        Object[] valoresAntigos = valores;
        int capacidadeAntiga = capacidade;

        // Dobra a capacidade
        capacidade = capacidadeAntiga * 2;
        chaves = new Object[capacidade];
        valores = new Object[capacidade];
        tamanho = 0;

        // Reinserere todos os pares válidos
        for (int i = 0; i < capacidadeAntiga; i++) {
            if (chavasAntigas[i] != null && chavasAntigas[i] != DELETADO) {
                put((K) chavasAntigas[i], (V) valoresAntigos[i]);
            }
        }
    }

    public double fatorDeCarga() {
        return (double) tamanho / capacidade;
    }

    public int tamanho() {
        return tamanho;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    public int capacidade() {
        return capacidade;
    }
}
