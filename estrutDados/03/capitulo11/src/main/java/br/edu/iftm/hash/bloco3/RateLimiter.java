package br.edu.iftm.hash.bloco3;

import java.util.*;

/**
 * Problema 3.3 - Rate Limiting por IP com Janela Deslizante
 * 
 * Uma API REST limita requisições por IP: máximo 100 requisições por minuto por cliente.
 * A estratégia é janela deslizante simplificada: para cada IP, armazene um contador
 * e o timestamp do início da janela atual.
 */
public class RateLimiter {
    private static final int LIMITE = 100;
    private static final long JANELA_MS = 60_000L; // 1 minuto em milissegundos

    // chave: IP do cliente (String)
    // valor: registro com contador e início da janela
    private final Map<String, Janela> contadores = new HashMap<>();

    /**
     * Retorna true se a requisição for permitida, false se o limite foi atingido.
     */
    public boolean permitir(String ip) {
        if (ip == null || ip.isEmpty()) {
            throw new IllegalArgumentException("IP não pode ser nulo ou vazio");
        }

        long agora = System.currentTimeMillis();
        Janela janela = contadores.get(ip);

        if (janela == null || agora - janela.inicioMs >= JANELA_MS) {
            // Janela expirou ou IP novo — reinicia a janela
            contadores.put(ip, new Janela(agora, 1));
            return true;
        }

        if (janela.contador < LIMITE) {
            janela.contador++;
            return true;
        }

        return false; // limite atingido
    }

    /**
     * Remove entradas de IPs cuja janela já expirou.
     * Útil para evitar crescimento ilimitado da tabela em produção.
     */
    public void limparExpirados() {
        long agora = System.currentTimeMillis();
        
        // Usar Iterator para segurança ao remover durante iteração
        Iterator<Map.Entry<String, Janela>> iterator = contadores.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Janela> entry = iterator.next();
            if (agora - entry.getValue().inicioMs >= JANELA_MS) {
                iterator.remove();
            }
        }
    }

    /**
     * Limpa todas as entradas (útil para testes)
     */
    public void limparTudo() {
        contadores.clear();
    }

    /**
     * Retorna o número de IPs com contador ativo
     */
    public int totalIPsAtivos() {
        return contadores.size();
    }

    /**
     * Classe interna para armazenar janela de requisições
     */
    private static class Janela {
        long inicioMs;
        int contador;

        Janela(long inicioMs, int contador) {
            this.inicioMs = inicioMs;
            this.contador = contador;
        }
    }
}
