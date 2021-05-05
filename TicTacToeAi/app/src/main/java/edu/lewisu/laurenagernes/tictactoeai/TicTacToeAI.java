package edu.lewisu.laurenagernes.tictactoeai;

// Import declaration.
import android.util.Log;

import java.lang.Math;
import java.util.Arrays;

public class TicTacToeAI {
    // Variable declaration.
    static int boardSize = 3;
    static char[][] marks = {
            {' ',' ',' '},
            {' ',' ',' '},
            {' ',' ',' '}
    };

    public void setBoard() {
        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                marks[i][j] = ' ';
            }
        }
    }

    public void setMarks(int row, int col) {
        final String TAG = "setMarks";
        marks[row][col] = 'X';
        Log.d(TAG, "setMarks Grid: " + Arrays.deepToString(marks));
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

    public int[] makeCompMove() {
        final String TAG = "TicTacToeAI";

        // Variable declaration.
        float bestScore = Integer.MAX_VALUE;
        float alpha = Integer.MIN_VALUE;
        float beta = Integer.MAX_VALUE;
        int depth = 5;
        int score = 0;
        int row = -1;
        int col = -1;
        int[] tempList = {0, 0};

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
        Log.d(TAG, "after AI move: " + Arrays.deepToString(marks));
        tempList[0] = row;
        tempList[1] = col;
        return tempList;
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
