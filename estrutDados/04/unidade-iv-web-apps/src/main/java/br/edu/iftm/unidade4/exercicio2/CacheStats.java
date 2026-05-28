package br.edu.iftm.unidade4.exercicio2;

public class CacheStats {
    private final int entradas;
    private final long hits;
    private final long misses;
    private final double taxaAcerto;
    private final int capacidade;

    public CacheStats(int entradas, long hits, long misses, double taxaAcerto, int capacidade) {
        this.entradas = entradas;
        this.hits = hits;
        this.misses = misses;
        this.taxaAcerto = taxaAcerto;
        this.capacidade = capacidade;
    }

    public int getEntradas() {
        return entradas;
    }

    public long getHits() {
        return hits;
    }

    public long getMisses() {
        return misses;
    }

    public double getTaxaAcerto() {
        return taxaAcerto;
    }

    public int getCapacidade() {
        return capacidade;
    }
}
