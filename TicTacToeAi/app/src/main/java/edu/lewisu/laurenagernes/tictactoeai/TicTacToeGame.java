package edu.lewisu.laurenagernes.tictactoeai;

// Import declaration.
import android.util.Log;

import java.util.Arrays;

public class TicTacToeGame {
    private static final String TAG = "TicTacToeGame";
    public static final int size = 3;
    public static final int NUM_ROWS = size;
    public static final int NUM_COLS = size;

    // Squares that make up the grid
    private char[][] mGridSquare;

    // Constructor
    public TicTacToeGame() {
        mGridSquare = new char[NUM_ROWS][NUM_COLS];
    }

    public void newGame() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                mGridSquare[row][col] = ' ';
            }
        }
        Log.d(TAG, "grid1: " + Arrays.deepToString(mGridSquare));
    }

    // checks if square is filled
    public boolean isOnBoard(int row, int col) {
        if (mGridSquare[row][col] == ' ') {
            return false;
        } else {
            return true;
        }
    }

    // select square -- alternates between true and false
    public void selectGridSquare(int row, int col) {
        mGridSquare[row][col] = 'x';
        Log.d(TAG, "grid3: " + Arrays.deepToString(mGridSquare));
    }

    // controls when game is over
    public boolean isGameOver() {
        int countXs = 0;
        // check rows
        for (int row = 0; row < NUM_ROWS; row++) {
            countXs = 0;
            for (int col = 0; col < NUM_COLS; col++) {
                if (mGridSquare[row][col] == 'x') { countXs++; }
            }
            if(countXs == size) { return true; }
        }

        // check columns
        for (int row = 0; row < NUM_ROWS; row++) {
            countXs = 0;
            for (int col = 0; col < NUM_COLS; col++) {
                if (mGridSquare[col][row] == 'x') { countXs++; }
            }
            if(countXs == size) { return true; }
        }

        countXs = 0;

        // check diagonal from left to right
        for (int row = 0, col = 0; row < size; row++, col++) {
            if (mGridSquare[row][col] == 'x') { countXs++; }
        }
        if (countXs == size) { return true; }

        countXs = 0;

        // check diagonal from right to left
        for (int row = size-1, col = 0; row > -1; row--, col++) {
            if (mGridSquare[row][col] == 'x') { countXs++; }
        }
        if (countXs == size) { return true; }

        return false;
    }
}
