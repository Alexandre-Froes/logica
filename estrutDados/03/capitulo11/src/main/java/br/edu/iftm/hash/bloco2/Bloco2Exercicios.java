package br.edu.iftm.hash.bloco2;

/**
 * Exercício 2.1 - Rastreamento Manual: Sondagem Linear e Quadrática
 * 
 * Tabela hash com capacidade M = 11, função hash h(k) = k % 11
 * Sequência: inserir(20), inserir(31), inserir(54), inserir(43), inserir(65), inserir(9)
 * 
 * PARTE A - Sondagem Linear: h(k, i) = (h(k) + i) % M
 * 
 * 20 % 11 = 9  → Slot 9 livre → Final: Slot 9
 * 31 % 11 = 9  → Slot 9 ocupado, i=1 → (9+1)%11=10 livre → Final: Slot 10
 * 54 % 11 = 10 → Slot 10 ocupado, i=1 → (10+1)%11=0 livre → Final: Slot 0
 * 43 % 11 = 10 → Slot 10 ocupado, i=1 → (10+1)%11=0 ocupado, i=2 → (10+2)%11=1 livre → Final: Slot 1
 * 65 % 11 = 10 → Slot 10 ocupado, i=1 → (10+1)%11=0 ocupado, i=2 → (10+2)%11=1 ocupado, i=3 → (10+3)%11=2 livre → Final: Slot 2
 * 9 % 11 = 9   → Slot 9 ocupado, i=1 → (9+1)%11=10 ocupado, i=2 → (9+2)%11=0 ocupado, i=3 → (9+3)%11=1 ocupado, i=4 → (9+4)%11=2 ocupado, i=5 → (9+5)%11=3 livre → Final: Slot 3
 * 
 * RESPOSTAS:
 * 
 * 5. Qual é o fenômeno que ocorre quando chaves com o mesmo h(k) inicial formam um bloco contíguo?
 *    Resposta: Clustering primário (primary clustering)
 *    - Quando múltiplas chaves com colisão são armazenadas em posições contíguas,
 *      as sondagens subsequentes examinam essas mesmas células repetidamente
 *    - Isso piora o desempenho, tornando as buscas mais lentas
 *    - A sondagem quadrática h(k, i) = (h(k) + i²) % M atenua esse problema
 *      porque os saltos aumentam (0, 1, 4, 9, 16...), pulando as células densas
 * 
 * 6. Calcule o fator de carga após todas as inserções.
 *    λ = n/M = 6/11 ≈ 0.545
 *    Sim, está dentro do limite recomendado para endereçamento aberto (λ < 0.70)
 */
class Bloco2Exercicio2_1 {
    // Apenas documentação
}

/**
 * Exercício 2.2 - Double Hashing
 * 
 * Tabela de capacidade M = 11
 * h1(k) = k % 11
 * h2(k) = 7 - (k % 7)
 * h(k, i) = (h1(k) + i * h2(k)) % 11
 * 
 * Inserir: 76, 40, 48, 5, 55, 47
 * 
 * 76: h1(76) = 76 % 11 = 10, h2(76) = 7 - (76 % 7) = 7 - 6 = 1
 *     h(76, 0) = 10 → Slot 10 livre → Final: 10
 * 
 * 40: h1(40) = 40 % 11 = 7, h2(40) = 7 - (40 % 7) = 7 - 5 = 2
 *     h(40, 0) = 7 → Slot 7 livre → Final: 7
 * 
 * 48: h1(48) = 48 % 11 = 4, h2(48) = 7 - (48 % 7) = 7 - 6 = 1
 *     h(48, 0) = 4 → Slot 4 livre → Final: 4
 * 
 * 5:  h1(5) = 5 % 11 = 5, h2(5) = 7 - (5 % 7) = 7 - 5 = 2
 *     h(5, 0) = 5 → Slot 5 livre → Final: 5
 * 
 * 55: h1(55) = 55 % 11 = 0, h2(55) = 7 - (55 % 7) = 7 - 6 = 1
 *     h(55, 0) = 0 → Slot 0 livre → Final: 0
 * 
 * 47: h1(47) = 47 % 11 = 3, h2(47) = 7 - (47 % 7) = 7 - 5 = 2
 *     h(47, 0) = 3 → Slot 3 livre → Final: 3
 * 
 * Distribuição final: todos encontraram slots livres na primeira sonda!
 * Não houve colisões neste exemplo.
 */
class Bloco2Exercicio2_2 {
    // Apenas documentação
}
