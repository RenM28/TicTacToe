package edu.lewisu.laurenagernes.tictactoeai;

// Import declaration.
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // game score variables
    private int winScore;
    private int drawScore;
    private int lossScore;

    // widgets
    private TicTacToeGame mGame;
    private ImageView[][] mGridSquares;
    private Button mRetryButton;
    private TextView mStatusTextView;
    private TicTacToeAI mAI;
    private TextView mWinScoreTextView;
    private TextView mDrawScoreTextView;
    private TextView mLossScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences("mypref", 0);
        winScore = sharedPref.getInt("win_score", 0);
        drawScore = sharedPref.getInt("draw_score", 0);
        lossScore = sharedPref.getInt("loss_score", 0);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mRetryButton = findViewById(R.id.button_new_game);
        mStatusTextView = findViewById(R.id.text_view_status);

        mWinScoreTextView = findViewById(R.id.winScoreTextView);
        mDrawScoreTextView = findViewById(R.id.drawScoreTextView);
        mLossScoreTextView = findViewById(R.id.lossScoreTextView);

        mGridSquares = new ImageView[TicTacToeGame.NUM_ROWS][TicTacToeGame.NUM_COLS];
        mAI = new TicTacToeAI();

        GridLayout gridLayout = findViewById(R.id.tic_tac_toe_grid);
        int childIndex = 0;
        for (int row = 0; row < TicTacToeGame.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacToeGame.NUM_COLS; col++) {
                Log.d(TAG, "grid4: " + Arrays.deepToString(mGridSquares));
                mGridSquares[row][col] = (ImageView) gridLayout.getChildAt(childIndex);
                Log.d(TAG, "grid5: " + Arrays.deepToString(mGridSquares));
                childIndex++;
            }
        }

        mGame = new TicTacToeGame();
        mAI.setBoard(); // Resets game board in AI file.
        startGame();

        if(savedInstanceState != null) {
            winScore = savedInstanceState.getInt("winScore");
            drawScore = savedInstanceState.getInt("drawScore");
            lossScore = savedInstanceState.getInt("lossScore");
        }
        updateScore(); // updates score when app opens

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.primaryLightColorBlue));
        }
    }

    private void startGame() {
        mGame.newGame();
        setImageViewDisplay();
    }

    private void updateScore() {
        String scoreString = getResources().getString(R.string.win_score, winScore);
        mWinScoreTextView.setText(scoreString);

        scoreString = getResources().getString(R.string.draw_score, drawScore);
        mDrawScoreTextView.setText(scoreString);

        scoreString = getResources().getString(R.string.loss_score, lossScore);
        mLossScoreTextView.setText(scoreString);
    }

    public void onGridImageViewClick(View view) {
        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        SharedPreferences.Editor editor= sharedPref.edit();

        // Find the row and col selected
        boolean imageViewFound = false;
        for (int row = 0; row < TicTacToeGame.NUM_ROWS && !imageViewFound; row++) {
            for (int col = 0; col < TicTacToeGame.NUM_COLS && !imageViewFound; col++) {
                if (view == mGridSquares[row][col]) {
                    mGame.selectGridSquare(row, col);
                    imageViewFound = true;
                }
            }
        }

        setImageViewDisplay();

        // Checks if game is won, lost, or if it is a draw.
        if (mAI.checkWin('X')) {  // Won or complete row of X.
            Log.d(TAG, "check win: " + "won");
            mStatusTextView.setText(R.string.game_won);
            mStatusTextView.setVisibility(View.VISIBLE);
            mRetryButton.setVisibility(View.VISIBLE);
            winScore ++;
            updateScore();

            // disable board
            for (int row = 0; row < TicTacToeGame.NUM_ROWS; row++) {
                for (int col = 0; col < TicTacToeGame.NUM_COLS; col++) {
                    mGridSquares[row][col].setEnabled(false);
                }
            }

            // Saves current scores.
            editor.putInt("loss_score", lossScore);
            editor.putInt("draw_score", drawScore);
            editor.putInt("win_score", winScore);
            editor.commit();
        } else if (mAI.checkWin('O')) {   // Lost or complete row of O.
            Log.d(TAG, "check win: " + "lost");
            mStatusTextView.setText(R.string.game_lost);
            mStatusTextView.setVisibility(View.VISIBLE);
            mRetryButton.setVisibility(View.VISIBLE);
            lossScore ++;
            updateScore();

            // disable board
            for (int row = 0; row < TicTacToeGame.NUM_ROWS; row++) {
                for (int col = 0; col < TicTacToeGame.NUM_COLS; col++) {
                    mGridSquares[row][col].setEnabled(false);
                }
            }

            // Saves current scores.
            editor.putInt("loss_score", lossScore);
            editor.putInt("draw_score", drawScore);
            editor.putInt("win_score", winScore);
            editor.commit();
        } else if (mAI.noMoreMoves()) {   // Draw or no complete rows and no free spaces.
            Log.d(TAG, "check win: " + "draw");
            mStatusTextView.setText(R.string.game_tied);
            mStatusTextView.setVisibility(View.VISIBLE);
            mRetryButton.setVisibility(View.VISIBLE);
            drawScore ++;
            updateScore();

            // disable board
            for (int row = 0; row < TicTacToeGame.NUM_ROWS; row++) {
                for (int col = 0; col < TicTacToeGame.NUM_COLS; col++) {
                    mGridSquares[row][col].setEnabled(false);
                }
            }

            // Saves current scores.
            editor.putInt("loss_score", lossScore);
            editor.putInt("draw_score", drawScore);
            editor.putInt("win_score", winScore);
            editor.commit();
        }
    }

    private void setImageViewDisplay() {
        // Variable declaration.
        int comp_row;
        int comp_col;
        int[] tempCoordinates;
        boolean isAITurn = false;

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        Log.d(TAG, "currentNightMode: " + currentNightMode);

        // Player's move.
        for (int row = 0; row < TicTacToeGame.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacToeGame.NUM_COLS; col++) {
                if (mGame.isOnBoard(row, col)) {
                    if (currentNightMode == 32) {   // Dark mode.
                        mGridSquares[row][col].setEnabled(false);
                        mAI.setMarks(row, col);   // Passes coordinates to AI file to save in "marks" array. This will keep track of "X" placement.
                        mGridSquares[row][col].setImageResource(R.drawable.ic_close_white_18dp);
                        isAITurn = true;
                    } else if (currentNightMode == 16) {    // Light mode.
                        mGridSquares[row][col].setEnabled(false);
                        mAI.setMarks(row, col);   // Passes coordinates to AI file to save in "marks" array. This will keep track of "X" placement.
                        mGridSquares[row][col].setImageResource(R.drawable.ic_close_black_18dp);
                        isAITurn = true;
                    } else {    // Don't know what mode we're in, assume light.
                        mGridSquares[row][col].setEnabled(false);
                        mAI.setMarks(row, col);   // Passes coordinates to AI file to save in "marks" array. This will keep track of "X" placement.
                        mGridSquares[row][col].setImageResource(R.drawable.ic_close_black_18dp);
                        isAITurn= true;
                    }
                }
            }
        }

        // Computes AI move.
        if (isAITurn == true & !mAI.noMoreMoves()) {
            tempCoordinates = mAI.makeCompMove();
            Log.d(TAG, "tempCoordinates: " + Arrays.toString(tempCoordinates));

            comp_row = tempCoordinates[0];
            comp_col = tempCoordinates[1];
            Log.d(TAG, "comp_row: " + comp_row);
            Log.d(TAG, "comp_col: " + comp_col);

            if (currentNightMode == 32) {   // Dark mode.
                mGridSquares[comp_row][comp_col].setEnabled(false);
                mGridSquares[comp_row][comp_col].setImageResource(R.drawable.ic_circle_outline_white_18dp);
            } else if (currentNightMode == 16) {    // Light mode.
                mGridSquares[comp_row][comp_col].setEnabled(false);
                mGridSquares[comp_row][comp_col].setImageResource(R.drawable.ic_circle_outline_black_18dp);
            } else {    // Don't know what mode we're in, assume light.
                mGridSquares[comp_row][comp_col].setEnabled(false);
                mGridSquares[comp_row][comp_col].setImageResource(R.drawable.ic_circle_outline_black_18dp);
            }
        }
    }

    public void onNewGameClick(View view) {
        mGame.newGame(); // reset all image views to false
        mAI.setBoard(); // Resets game board in AI file.

        // reset image views and enable board
        for (int row = 0; row < TicTacToeGame.NUM_ROWS; row++) {
            for (int col = 0; col < TicTacToeGame.NUM_COLS; col++) {
                mGridSquares[row][col].setImageResource(0);
                mGridSquares[row][col].setEnabled(true);
            }
        }

        // clear button and text view visibility
        mStatusTextView.setVisibility(View.GONE);
        mRetryButton.setVisibility(View.GONE);
    }

    // saves all scores in bundle
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("winScore", winScore);
        outState.putInt("drawScore", drawScore);
        outState.putInt("lossScore", lossScore);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
