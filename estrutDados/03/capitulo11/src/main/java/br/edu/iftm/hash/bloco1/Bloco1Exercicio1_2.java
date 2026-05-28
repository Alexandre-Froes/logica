package br.edu.iftm.hash.bloco1;

/**
 * Exercício 1.2 - Rastreamento Manual: Encadeamento Separado
 * 
 * Tabela hash com capacidade M = 7, função hash h(k) = k % 7
 * Sequência de inserções: inserir(10), inserir(22), inserir(31), inserir(4), inserir(15), inserir(28), inserir(17)
 * 
 * RESPOSTAS:
 * 
 * 1. Qual é o fator de carga λ = n/M após todas as inserções?
 *    λ = 7 / 7 = 1.0
 * 
 * 2. Qual é o comprimento da cadeia mais longa?
 *    Bucket 3: [10, 31, 17] → comprimento = 3
 *    Qual operação tem custo O(n) no pior caso?
 *    get() ou remove() podem ter custo O(n) se precisarem percorrer toda a cadeia
 * 
 * 3. Se inseríssemos a chave 38, em qual bucket ela seria alocada?
 *    38 % 7 = 3 → seria alocada no bucket 3
 *    Haveria colisão? SIM, pois o bucket 3 já tem [10, 31, 17]
 * 
 * 4. A função h(k) = k % 7 distribui bem as chaves desta sequência?
 *    Não. A distribuição é ruim:
 *    Bucket 0: [28] (1 elemento)
 *    Bucket 1: [22, 15] (2 elementos)
 *    Bucket 3: [10, 31, 17] (3 elementos)
 *    Bucket 4: [4] (1 elemento)
 * 
 *    Pior distribuição para h(k) = k % 7:
 *    Inserir chaves: 0, 7, 14, 21, 28, 35, 42
 *    Todas resultariam em: h(k) = 0 → todas no bucket 0, causando a pior distribuição (catastrófica)
 */
public class Bloco1Exercicio1_2 {
    // Apenas uma classe para documentação das respostas
}
