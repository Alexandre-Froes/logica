package br.edu.iftm.hash.bloco3;

import java.util.*;

/**
 * Problema 3.2 - Índice Invertido para Busca de Produtos
 * 
 * Um sistema de e-commerce precisa de busca por palavra-chave em nomes de produtos.
 * A estrutura é um índice invertido: cada palavra mapeia para a lista de IDs dos produtos.
 */
public class IndiceInvertido {
    // chave: palavra normalizada (minúscula, sem acentos)
    // valor: lista de IDs de produtos que contêm a palavra
    private final Map<String, List<Long>> indice = new HashMap<>();

    /**
     * Indexa o produto. Normaliza o nome antes de dividir em palavras.
     */
    public void indexar(long idProduto, String nomeProduto) {
        if (nomeProduto == null || nomeProduto.isEmpty()) {
            return;
        }

        // Normalizar: converter para minúscula e dividir por espaços
        String[] palavras = nomeProduto.toLowerCase().split("\\s+");

        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                // Obter ou criar lista de IDs para esta palavra
                indice.computeIfAbsent(palavra, k -> new ArrayList<>())
                      .add(idProduto);
            }
        }
    }

    /**
     * Retorna a lista de IDs que contêm a palavra, ou lista vazia se não encontrada.
     */
    public List<Long> buscar(String palavra) {
        if (palavra == null || palavra.isEmpty()) {
            return new ArrayList<>();
        }

        String palavraNormalizada = palavra.toLowerCase();
        List<Long> resultados = indice.get(palavraNormalizada);
        return resultados != null ? new ArrayList<>(resultados) : new ArrayList<>();
    }

    /**
     * Retorna os IDs que aparecem em TODAS as palavras da consulta (interseção).
     * Exemplo: buscar("cadeira gamer") → [3]
     */
    public List<Long> buscarMultiplas(String consulta) {
        if (consulta == null || consulta.isEmpty()) {
            return new ArrayList<>();
        }

        String[] palavras = consulta.toLowerCase().split("\\s+");
        List<Long> resultado = null;

        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                List<Long> idsParaPalavra = indice.getOrDefault(palavra, new ArrayList<>());
                
                if (resultado == null) {
                    resultado = new ArrayList<>(idsParaPalavra);
                } else {
                    // Interseção: manter apenas IDs que estão em ambas as listas
                    resultado.retainAll(idsParaPalavra);
                }
            }
        }

        return resultado != null ? resultado : new ArrayList<>();
    }

    public int totalPalavrasIndexadas() {
        return indice.size();
    }
}
