package ticTacToe;

/**
 * Jogo da veia
 *
 * @version 1.0
 * @since 2023-10-20
 * @author Diógenes Dietrich
 *
 */
public class TicTacToe {
    /**
     * Tamanho do tabuleiro 3x3
     */
    private static final int SIZE = 3;
    /**
     * Símbolos do jogo para jogadores
     */
    private static final char[] SYMBOLS = {'X', 'O'};
    /**
     * Símbolo para célula vazia
     */
    private static final char EMPTY = '_';
    /**
     * Tabuleiro do jogo
     */
    private static final char[][] FIELD = new char[SIZE][SIZE];
    /**
     * Jogador atual
     */
    private static char currentPlayer = SYMBOLS[0];
    /**
     * Se o jogo é contra o computador
     */
    private static boolean isAgainstComputer = false;

    /**
     * Enum para dificuldade do jogo
     */
    private static enum EnumDifficulty {
        POSSIBLE,
        IMPOSSIBLE
    }

    /**
     * Dificuldade do jogo
     */
    private static EnumDifficulty difficulty = EnumDifficulty.POSSIBLE;

    /**
     * Registro para armazenar a jogada
     * @param row Linha
     * @param col Coluna
     * @param score Pontuação baseada no minimax
     */
    private record Move(int row, int col, int score) {}

    /**
     * Inicializa o tabuleiro com células vazias
     */
    private static void initField() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                FIELD[i][j] = EMPTY;
            }
        }
    }

    /**
     * Imprime o tabuleiro no console
     */
    private static void printField() {
        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + 1 + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + 1 + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(FIELD[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Verifica se a célula está vazia
     *
     * @param row Linha
     * @param col Coluna
     *
     * @return Se a célula está vazia
     */
    private static boolean isCellEmpty(int row, int col) {
        return FIELD[row][col] == EMPTY;
    }

    /**
     * Verifica se a célula é válida
     *
     * @param row Linha
     * @param col Coluna
     *
     * @return Se a célula é válida
     */
    private static boolean isCellValid(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    /**
     * Verifica se a célula está livre
     *
     * @param row Linha
     * @param col Coluna
     *
     * @return Se a célula está livre
     */
    private static boolean isCellFree(int row, int col) {
        return isCellValid(row, col) && isCellEmpty(row, col);
    }

    /**
     * Verifica se o jogo terminou empatado
     *
     * @return Se o jogo terminou empatado
     */
    private static boolean isDraw() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (FIELD[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Verifica se o jogador ganhou
     *
     * @param symbol Símbolo do jogador a ser verificado
     *
     * @return Se o jogador ganhou
     */
    private static boolean isWin(char symbol) {
        for (int i = 0; i < SIZE; i++) {
            if (FIELD[i][0] == symbol && FIELD[i][1] == symbol && FIELD[i][2] == symbol) {
                return true;
            }
            if (FIELD[0][i] == symbol && FIELD[1][i] == symbol && FIELD[2][i] == symbol) {
                return true;
            }
        }
        if (FIELD[0][0] == symbol && FIELD[1][1] == symbol && FIELD[2][2] == symbol) {
            return true;
        }
        if (FIELD[0][2] == symbol && FIELD[1][1] == symbol && FIELD[2][0] == symbol) {
            return true;
        }
        return false;
    }

    /**
     * Faz a jogada
     * @param move Jogada
     * @param symbol Símbolo do jogador
     */
    private static void makeMove(Move move, char symbol) {
        FIELD[move.row][move.col] = symbol;
    }

    /**
     * Algoritmo minimax para encontrar a melhor jogada
     *
     * @param depht Profundidade da árvore
     * @param isMax Se está encontrando a melhor jogada para o jogador
     *
     * @return
     */
    public static int minimax(int depht, boolean isMax){
        // Se o jogador ganhou, retorna -10, pois vamos maximizar sempre a máquina
        if (isWin(SYMBOLS[0])) {
            return -10;
        }

        // Se a máquina ganhou, retorna 10, pois vamos maximizar sempre a máquina
        if (isWin(SYMBOLS[1])) {
            return 10;
        }

        // Se o jogo terminou empatado, retorna 0
        if (isDraw()) {
            return 0;
        }

        if (isMax) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++){
                    if (isCellFree(i, j)) {
                        // Faz a jogada
                        makeMove(new Move(i, j, 0), SYMBOLS[1]);
                        // Chama o minimax recursivamente para a próxima jogada e maximiza o resultado
                        // Assim verifica o melhor score para a máquina
                        int score = Math.max(bestScore, minimax(depht + 1, false));
                        // Desfaz a jogada
                        makeMove(new Move(i, j, 0), EMPTY);

                        // Atualiza o melhor score
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }

            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++){
                    if (isCellFree(i, j)) {
                        // Faz a jogada
                        makeMove(new Move(i, j, 0), SYMBOLS[0]);
                        // Chama o minimax recursivamente para a próxima jogada e minimiza o resultado
                        int score = Math.min(bestScore, minimax(depht + 1, true));
                        // Desfaz a jogada
                        makeMove(new Move(i, j, 0), EMPTY);

                        // Atualiza o melhor score
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }

            return bestScore;
        }
    }

    public static Move findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        Move bestMove = new Move(-1, -1, 0);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++){
                if (isCellFree(i, j)) {
                    // Faz a jogada
                    makeMove(new Move(i, j, 0), SYMBOLS[1]);
                    // Chama o minimax recursivamente para a próxima jogada e maximiza o resultado
                    int score = minimax(0, false);
                    // Desfaz a jogada
                    makeMove(new Move(i, j, 0), EMPTY);

                    // Substitui o melhor score e a melhor jogada caso o score seja maior
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove = new Move(i, j, score);
                    }
                }
            }
        }

        return bestMove;
    }

    /**
     * Inicia o jogo
     */
    public static void play() {
        initField();

        while (true) {
            System.out.print("Do you want to play against computer? (y/n): ");
            String input = new java.util.Scanner(System.in).nextLine();
            if (input.equals("y")) {
                isAgainstComputer = true;

                System.out.println("Choose difficulty:");
                System.out.println("1. Possible");
                System.out.println("2. Impossible");
                System.out.print("Your choice: ");
                input = new java.util.Scanner(System.in).nextLine();

                if (input.equals("1")) {
                    difficulty = EnumDifficulty.POSSIBLE;
                } else if (input.equals("2")) {
                    difficulty = EnumDifficulty.IMPOSSIBLE;
                } else {
                    System.out.println("Invalid difficulty!");
                    continue;
                }

                break;
            } else if (input.equals("n")) {
                isAgainstComputer = false;
                break;
            }
        }

        Move move = new Move(-1, -1, 0);
        int step = 0;

        //Sorteia o jogador inicial
        if (Math.random() < 0.5) {
            currentPlayer = SYMBOLS[0];
        } else {
            currentPlayer = SYMBOLS[1];
        }

        System.out.println(currentPlayer + " starts the game!");

        while (true) {
            while (true) {
                String[] input;

                // Se o jogador for a máquina, chama o algoritmo minimax para encontrar a melhor jogada
                if (isAgainstComputer && currentPlayer == SYMBOLS[1]) {
                    if (difficulty == EnumDifficulty.POSSIBLE) {
                        move = new Move((int) (Math.random() * SIZE), (int) (Math.random() * SIZE), 0);

                    } else if (difficulty == EnumDifficulty.IMPOSSIBLE) {
                        move = findBestMove();
                    }
                } else {
                    System.out.println(currentPlayer + "'s turn");
                    System.out.print("Enter the coordinates: ");
                    input = new java.util.Scanner(System.in).nextLine().split(" ");
                    try {
                        move = new Move(Integer.parseInt(input[0]) - 1, Integer.parseInt(input[1]) - 1, 0);
                    } catch (Exception e) {
                        System.out.println("You should enter numbers!");
                        continue;
                    }
                    if (!isCellValid(move.row, move.col)) {
                        System.out.println("Coordinates should be from 1 to 3!");
                        continue;
                    }
                    if (!isCellFree(move.row, move.col)) {
                        System.out.println("This cell is occupied! Choose another one!");
                        continue;
                    }
                }

                break;
            }
            makeMove(move, currentPlayer);
            printField();

            if (isWin(currentPlayer)) {
                System.out.println(currentPlayer + " wins");
                break;
            }
            if (isDraw()) {
                System.out.println("Draw");
                break;
            }

            // Troca o jogador
            currentPlayer = currentPlayer == SYMBOLS[0] ? SYMBOLS[1] : SYMBOLS[0];
            step++;
        }
    }
}
