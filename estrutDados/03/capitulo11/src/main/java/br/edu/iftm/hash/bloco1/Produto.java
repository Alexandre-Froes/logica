package br.edu.iftm.hash.bloco1;

import java.util.Objects;

/**
 * Classe A - Produto
 * Problema: hashCode() não foi sobrescrito
 * Solução: Implementar hashCode() consistente com equals()
 */
public class Produto {
    private String sku;
    private String nome;

    public Produto(String sku, String nome) {
        this.sku = sku;
        this.nome = nome;
    }

    public String getSku() {
        return sku;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produto)) return false;
        Produto p = (Produto) o;
        return sku.equals(p.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }
}
