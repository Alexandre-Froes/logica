package br.edu.iftm.hash.bloco3;

/**
 * Respostas às Perguntas de Análise do BLOCO 3
 */
public class Bloco3Respostas {
    /**
     * PROBLEMA 3.1 - Cache de Sessões HTTP
     * 
     * Pergunta 10: Qual é a complexidade esperada (caso médio) de registrar(), 
     * buscar() e invalidar() com encadeamento separado e fator de carga λ ≤ 0,75?
     * 
     * Resposta: O(1) amortizado
     * - Com λ ≤ 0,75, o comprimento médio de cada lista é aproximadamente 0,75
     * - Operações em listas com comprimento constante levam O(1)
     * - A operação de hashing é O(1)
     * - Portanto, todas as três operações são O(1) em caso médio
     * 
     * ---
     * 
     * Pergunta 11: Por que String é uma boa escolha como chave de tabela hash?
     * Quais propriedades da classe String garantem isso?
     * 
     * Resposta: String é excelente por:
     * - hashCode() é bem distribuído: usa algoritmo de hash robusto baseado em caracteres
     * - Imutabilidade: uma vez criada, não pode mudar, garantindo hashCode consistente
     * - equals() bem implementado: compara conteúdo, não referência
     * - Contrato hashCode/equals: ambos baseados no conteúdo da string
     * - Amplamente testado: seguro usar em produção
     * 
     * ---
     * 
     * Pergunta 12: Em produção, esse cache em memória apresentaria qual problema 
     * crítico em ambientes com múltiplos servidores? Qual tecnologia resolveria isso?
     * 
     * Resposta: Problema = Falta de sincronização entre servidores
     * - Cada servidor tem seu próprio cache em memória
     * - Uma sessão criada no servidor A não é reconhecida pelo servidor B
     * - Usuário pode ser deslogado ao ser redirecionado entre servidores
     * 
     * Soluções:
     * - Redis: cache distribuído, compartilhado entre todos os servidores
     * - Memcached: similar ao Redis
     * - Banco de dados: armazenar sessões de forma persistente
     * - Session affinity/sticky sessions: sempre enviar cliente para mesmo servidor
     */
    public static final String RESPOSTA_P10 = "O(1) amortizado";
    
    /**
     * PROBLEMA 3.2 - Índice Invertido
     * 
     * Pergunta 13: Qual é a complexidade de indexar() em função do número de 
     * palavras W do nome do produto?
     * 
     * Resposta: O(W) onde W = número de palavras
     * - Dividir string em palavras: O(W)
     * - Para cada palavra, insert em HashMap: O(1) amortizado
     * - Total: O(W)
     * 
     * ---
     * 
     * Pergunta 14: Qual é a complexidade de buscarMultiplas() em função do número 
     * de palavras na consulta Q e do tamanho máximo de uma lista de resultados R?
     * 
     * Resposta: O(Q * R) onde Q = palavras na consulta, R = tamanho máximo de lista
     * - Para cada uma das Q palavras, buscar em HashMap: O(1)
     * - Cada busca retorna lista de até R elementos
     * - Interseção (retainAll) entre listas: O(Q * R)
     * - Total: O(Q * R)
     * 
     * ---
     * 
     * Pergunta 15: HashMap<String, List<Long>> vs TreeMap<String, List<Long>>: 
     * qual escolher e por quê? Em que caso TreeMap seria preferível?
     * 
     * Resposta: Escolher HashMap por padrão
     * - HashMap: O(1) para busca, inserção, mais rápido em geral
     * - TreeMap: O(log N) para operações, mas oferece ordem
     * 
     * TreeMap seria preferível se:
     * - Precisar de palavras ordenadas alfabeticamente
     * - Precisar de busca por prefixo ou range (ex: buscar palavras entre "cadeira" e "mesa")
     * - Precisar iterar em ordem
     * - Requisito: apresentar sugestões ordenadas ao usuário
     */
    public static final String RESPOSTA_P13 = "O(W)";
    public static final String RESPOSTA_P14 = "O(Q * R)";
    
    /**
     * PROBLEMA 3.3 - Rate Limiting
     * 
     * Pergunta 16: Qual é a complexidade de permitir() no caso médio? 
     * E de limparExpirados() em função do número N de IPs ativos?
     * 
     * Resposta:
     * - permitir(): O(1) amortizado
     *   - Busca em HashMap: O(1)
     *   - Incremento de contador: O(1)
     *   - Inserção de nova entrada: O(1)
     * 
     * - limparExpirados(): O(N) onde N = número de IPs ativos
     *   - Itera sobre N entradas
     *   - Cada remoção é O(1)
     *   - Total: O(N)
     * 
     * ---
     * 
     * Pergunta 17: Por que não se deve usar Map.entrySet().forEach() com 
     * map.remove() dentro do lambda? Qual exceção seria lançada?
     * 
     * Resposta: Lançaria ConcurrentModificationException
     * - forEach usa um Iterator interno
     * - Modificar a estrutura do map durante iteração viola contrato do Iterator
     * - Solução: usar Iterator explícito com iterator.remove()
     *   ou usar removeIf() que é seguro
     * 
     * ---
     * 
     * Pergunta 18: Com 1 milhão de IPs distintos por hora, a tabela pode crescer 
     * indefinidamente. Além de limparExpirados(), cite uma estrutura do JCF que gera 
     * expiração automática por tempo de acesso.
     * 
     * Resposta: LinkedHashMap com accessOrder=true
     * - LinkedHashMap com accessOrder=true mantém ordem de acesso
     * - Pode ser estendido para criar um LRU Cache
     * - Override removeEldestEntry() para remover entrada menos recentemente usada
     * 
     * Alternativa: Guava Cache (biblioteca externa)
     * - com expireAfterAccess() ou expireAfterWrite()
     * - automático e thread-safe
     */
    public static final String RESPOSTA_P16 = "permitir: O(1), limparExpirados: O(N)";
}
