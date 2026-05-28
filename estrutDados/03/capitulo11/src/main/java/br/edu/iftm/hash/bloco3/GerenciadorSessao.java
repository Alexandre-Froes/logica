package br.edu.iftm.hash.bloco3;

import br.edu.iftm.hash.bloco1.TabelaHashEncadeada;

/**
 * Problema 3.1 - Cache de Sessões HTTP
 * 
 * Um sistema de autenticação armazena tokens de sessão em memória com acesso O(1).
 * Recuperar usuário por token em tempo O(1) amortizado.
 * Invalidar (remover) a sessão de um token específico.
 * Verificar se um token ainda é válido (está presente na tabela).
 */
public class GerenciadorSessao {
    private static final int CAPACIDADE_INICIAL = 256;
    
    private final TabelaHashEncadeada<String, Usuario> sessoes;

    public GerenciadorSessao() {
        // Capacidade 256 é adequada para início, pois:
        // - Fator de carga será mantido baixo
        // - Reduz colisões
        // - Adequado para servidor web com múltiplas sessões
        this.sessoes = new TabelaHashEncadeada<>(CAPACIDADE_INICIAL);
    }

    /**
     * Registra nova sessão.
     * Lança IllegalArgumentException se token for nulo ou vazio.
     */
    public void registrar(String token, Usuario usuario) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token não pode ser nulo ou vazio");
        }
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        sessoes.put(token, usuario);
    }

    /**
     * Retorna o Usuario ou null se a sessão não existir.
     */
    public Usuario buscar(String token) {
        return sessoes.get(token);
    }

    /**
     * Remove a sessão. Retorna true se existia, false se já estava expirada/inválida.
     */
    public boolean invalidar(String token) {
        return sessoes.remove(token);
    }

    /**
     * Retorna true se o token ainda é válido.
     */
    public boolean eValido(String token) {
        return sessoes.get(token) != null;
    }

    /**
     * Retorna o número de sessões ativas.
     */
    public int totalSessoes() {
        return sessoes.tamanho();
    }
}
