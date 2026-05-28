package br.edu.iftm.hash.bloco1;

import java.util.Objects;

/**
 * Classe B - Sessao
 * Problema: hashCode() retorna sempre a mesma constante (42)
 * Isso causa degradação em HashMap/HashSet pois todas as chaves
 * vão para o mesmo bucket (clustering primário)
 * Solução: Implementar hashCode() baseado no token
 */
public class Sessao {
    private final String token;

    public Sessao(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sessao)) return false;
        return token.equals(((Sessao) o).token);
    }
}
