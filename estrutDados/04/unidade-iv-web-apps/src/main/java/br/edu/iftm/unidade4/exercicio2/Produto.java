package br.edu.iftm.unidade4.exercicio2;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Classe Produto para o cache LRU
 */
public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String sku;
    private BigDecimal preco;
    private Integer estoque;

    public Produto() {}

    public Produto(Long id, String nome, String sku, BigDecimal preco, Integer estoque) {
        this.id = id;
        this.nome = nome;
        this.sku = sku;
        this.preco = preco;
        this.estoque = estoque;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", sku='" + sku + '\'' +
                ", preco=" + preco +
                ", estoque=" + estoque +
                '}';
    }
}
