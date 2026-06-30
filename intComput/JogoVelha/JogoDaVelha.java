import java.util.Arrays;
import java.util.Scanner;

public class JogoDaVelha {

    static final char VAZIO = ' ';

    static final int[][] LINHAS_VITORIA = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},   
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
        {0, 4, 8}, {2, 4, 6}            
    };

    static char vencedor(char[] tab) {
        for (int[] l : LINHAS_VITORIA) {
            if (tab[l[0]] != VAZIO && tab[l[0]] == tab[l[1]] && tab[l[1]] == tab[l[2]]) {
                return tab[l[0]];
            }
        }
        return (char) 0;
    }

    static boolean tabuleiroCheio(char[] tab) {
        for (char c : tab) {
            if (c == VAZIO) return false;
        }
        return true;
    }

    static void mostrar(char[] tab) {
        System.out.println();
        for (int linha = 0; linha < 3; linha++) {
            StringBuilder sb = new StringBuilder("  ");
            for (int col = 0; col < 3; col++) {
                int i = linha * 3 + col;
                char cel = (tab[i] != VAZIO) ? tab[i] : (char) ('1' + i);
                sb.append(cel);
                if (col < 2) sb.append(" | ");
            }
            System.out.println(sb);
            if (linha < 2) System.out.println(" ---+---+---");
        }
        System.out.println();
    }

    static int minimax(char[] tab, boolean isMax, char comp, char hum) {
        char resultado = vencedor(tab);
        if (resultado == comp) return 10;
        if (resultado == hum) return -10;
        if (tabuleiroCheio(tab)) return 0;

        if (isMax) {
            int melhorPlacar = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (tab[i] == VAZIO) {
                    tab[i] = comp;
                    int placar = minimax(tab, false, comp, hum);
                    tab[i] = VAZIO;
                    melhorPlacar = Math.max(melhorPlacar, placar);
                }
            }
            return melhorPlacar;
        } else {
            int melhorPlacar = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (tab[i] == VAZIO) {
                    tab[i] = hum;
                    int placar = minimax(tab, true, comp, hum);
                    tab[i] = VAZIO;
                    melhorPlacar = Math.min(melhorPlacar, placar);
                }
            }
            return melhorPlacar;
        }
    }

    static int escolherJogada(char[] tab, char comp, char hum) {
        System.out.println("Pensando...");
        
        int melhorPlacar = Integer.MIN_VALUE;
        int melhorJogada = -1;

        for (int i = 0; i < 9; i++) {
            if (tab[i] == VAZIO) {
                tab[i] = comp;
                int placar = minimax(tab, false, comp, hum);
                tab[i] = VAZIO;

                if (placar > melhorPlacar) {
                    melhorPlacar = placar;
                    melhorJogada = i;
                }
            }
        }
        
        System.out.println("vou jogar na casa " + (melhorJogada + 1));
        return melhorJogada;
    }

    static int lerJogada(Scanner sc, char[] tab, char hum) {
        int pos = -1;
        while (pos < 0 || pos > 8 || tab[pos] != VAZIO) {
            System.out.print("Sua vez: (" + hum + ")  [1-9]: ");
            if (!sc.hasNextLine()) { System.out.println("\nflw vei!"); System.exit(0); }
            String linha = sc.nextLine().trim();
            try {
                pos = Integer.parseInt(linha) - 1;
            } catch (NumberFormatException e) {
                pos = -1;
            }
            if (pos < 0 || pos > 8) {
                System.out.println("  De 1 a 9");
            } else if (tab[pos] != VAZIO) {
                System.out.println("  Ja tem peça nessa casa");
            }
        }
        return pos;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        char hum = ' ';
        while (hum != 'X' && hum != 'O') {
            System.out.print("X ou O");
            if (!sc.hasNextLine()) { System.out.println("\nadeus..."); return; }
            String entrada = sc.nextLine().trim().toUpperCase();
            if (!entrada.isEmpty()) {
                hum = entrada.charAt(0);
            }
        }
        char comp = (hum == 'X') ? 'O' : 'X';
        System.out.println("\nVc eh o '" + hum + "' e comeca. eu sou o '" + comp + "'.");

        char[] tab = new char[9];
        Arrays.fill(tab, VAZIO);

        System.out.println("\ntabuleiro ta limpo, escolha um numero:");
        mostrar(tab);

        while (true) {
            int pos = lerJogada(sc, tab, hum);
            tab[pos] = hum;
            mostrar(tab);

            if (vencedor(tab) == hum) {
                System.out.println(" Você ganhou");
                break;
            }
            if (tabuleiroCheio(tab)) {
                System.out.println("velha");
                break;
            }

            int jogada = escolherJogada(tab, comp, hum);
            tab[jogada] = comp;
            System.out.println("CPU jogou na casa " + (jogada + 1) + ":");
            mostrar(tab);

            if (vencedor(tab) == comp) {
                System.out.println("CPU ganhou");
                break;
            }
            if (tabuleiroCheio(tab)) {
                System.out.println("velha");
                break;
            }
        }

        sc.close();
    }
}