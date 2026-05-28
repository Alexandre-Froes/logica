package br.edu.iftm.hash.bloco1;

import java.util.Objects;

/**
 * Classe C - Coordenada
 * Problema: equals() usa == para comparar double, o que é impreciso
 * doubles com ponto flutuante não devem ser comparados com ==
 * Solução: Usar Double.compare() ou fazer comparação com tolerância
 */
public class Coordenada {
    public double lat;
    public double lon;

    public Coordenada(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coordenada)) return false;
        Coordenada c = (Coordenada) o;
        // Usar Double.compare para comparação segura
        return Double.compare(c.lat, lat) == 0 && Double.compare(c.lon, lon) == 0;
    }
}
