// Name: George Pappas
// Date: April 11, 2021
// Time: 4:00 AM
// Project: Unbeatable TicTacToe game with AI

// Import declaration.
import java.lang.Math;
import java.util.Scanner;

public class TicTacToeAI {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println();

        // Variable declaration.
        int LOST = 0;
        int WON = 1;
        int DRAW = 2;
        int result;

        GenerateBoard board = new GenerateBoard();
        board.printBoard();

        while (true) {
            // Variable declaration.
            int row = -1;
            int col = -1;

            while (!(board.makeMove(row, col, 'X'))) {
                System.out.println("Player's Move");
                System.out.println("Choose the row: ");
                row = scan.nextInt();
                System.out.println("Choose the col: ");
                col = scan.nextInt();
            }

            board.printBoard(); // Display the board.

            if (board.checkWin('X')) {
                result = WON;
                break;
            } else if (board.noMoreMoves()) {
                result = DRAW;
                break;
            }

            board.makeCompMove();   // Makes computer moves.
            System.out.println();   // Creates new line.
            board.printBoard(); // Displays game board.

            if (board.checkWin('O')) {
                result = LOST;
                break;
            } else if (board.noMoreMoves()) {
                result = DRAW;
                break;
            }
        }

        System.out.println("GAME OVER");
        if (result == WON) {
            System.out.println("You Won!");
        } else if (result == LOST) {
            System.out.println("You Lost!");
        } else {
            System.out.println("It was a draw!");
        }
        scan.close();
    }
}

class GenerateBoard {
    // Variable declaration.
    static int boardSize = 3;
    static char[][] marks = {
        {' ',' ',' '},
        {' ',' ',' '},
        {' ',' ',' '}
    };

    public GenerateBoard() {
        // char marks[][] = new char[][] {};
        // Arrays.fill(marks, ' ');
    }

    public void printBoard() {
        for (int i=0; i<boardSize; i++) {
            System.out.print(" " + (i+1));
        }

        System.out.print("");
        for (int i=0; i<boardSize; i++) {
            System.out.println();   // Creates new line.
            for (int j=0; j<boardSize; j++) {
                System.out.print("--");
            }
            
            System.out.println("-");
            System.out.print(i+1);

            for (int j=0; j<boardSize; j++) {
                System.out.print("|" + marks[i][j]);
            }

            System.out.print("|");
        }

        System.out.println(" ");
        for (int i=0; i<boardSize; i++) {
            System.out.print("--");
        }
        System.out.println("-");
    }

    public boolean makeMove(int row, int col, char mark) {
        // Variable declaration.
        boolean possible = false;

        if (row == -1 & col == -1) {
            return false;
        }

        row = row - 1;
        col = col - 1;

        if (row < 0 || row >= boardSize || col < 0 || col >= boardSize) {
            System.out.println("Not a valid row or column!");
            return false;
        }

        if (marks[row][col] == ' ') {
            marks[row][col] = mark;
            possible = true;
        }

        if (possible == false & mark == 'X') {
            System.out.println("self position is already taken!");
        }

        return possible;
    }

    public boolean checkWin(char mark) {
        // Variable declaration.
        boolean won = false;

        // Checks each row.
        for (int i=0; i<boardSize; i++) {
            won = true;
            for (int j=0; j<boardSize; j++) {
                if (marks[i][j] != mark) {
                    won = false;
                    break;
                }
            }
            if (won == true) {
                break;
            }
        }

        // Checks each column.
        if (won == false) {
            for (int i=0; i<boardSize; i++) {
                won = true;
                for (int j=0; j<boardSize; j++) {
                    if (marks[j][i] != mark) {
                        won = false;
                        break;
                    }
                }
                if (won == true) {
                    break;
                }
            }
        }

        // Checks first diagonal.
        if (won == false) {
            for (int i=0; i<boardSize; i++) {
                won = true;
                if (marks[i][i] != mark) {
                    won = false;
                    break;
                }
            }
        }

        // Check second diagonal.
        if (won == false) {
            for (int i=0; i<boardSize; i++) {
                won = true;
                if (marks[boardSize-1-i][i] != mark) {
                    won = false;
                    break;
                }
            }
        }
        return won;
    }

    public boolean noMoreMoves() {
        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                if (marks[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public void makeCompMove() {
        // Variable declaration.
        float bestScore = Integer.MAX_VALUE;
        float alpha = Integer.MIN_VALUE;
        float beta = Integer.MAX_VALUE;
        int depth = 5;
        int score = 0;
        int row = -1;
        int col = -1;

        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                if (marks[i][j] == ' ') {
                    marks[i][j] = 'O';
                    score = minimax(depth, alpha, beta, true);
                    marks[i][j] = ' ';
                    if (score < bestScore) {
                        bestScore = score;
                        row = i;
                        col = j;
                    }
                }
            }
        }

        marks[row][col] = 'O';
        System.out.print("Computer choice: " + (row+1) + "," + (col+1));
    }

    public int minimax(int depth, float alpha, float beta, boolean isHuman) {
        if (checkWin('O') == true) {
            return -1;
        } else if (checkWin('X') == true) {
            return 1;
        } else if (depth == 0 | noMoreMoves()) {
            return 0;
        }

        // Human player.
        if (isHuman == true) {
            int maxEval = Integer.MIN_VALUE;

            for (int i=0; i<boardSize; i++) {
                for (int j=0; j<boardSize; j++) {
                    if (marks[i][j] == ' ') {
                        marks[i][j] = 'X';
                        int scoreEval = minimax(depth - 1, alpha, beta, false);
                        marks[i][j] = ' ';
                        maxEval = Math.max(scoreEval, maxEval);
                        alpha = Math.max(alpha, scoreEval);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;

            for (int i=0; i<boardSize; i++) {
                for (int j=0; j<boardSize; j++) {
                    if (marks[i][j] == ' ') {
                        marks[i][j] = 'O';
                        int scoreEval = minimax(depth - 1, alpha, beta, true);
                        marks[i][j] = ' ';
                        minEval = Math.min(scoreEval, minEval);
                        beta = Math.min(beta, scoreEval);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return minEval;
        }
    }
}
