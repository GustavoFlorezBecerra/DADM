package com.example.androidtictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidtictactoe.enums.DifficultyLevel;
import com.example.androidtictactoe.service.TicTacToeGame;
import com.example.androidtictactoe.view.BoardView;


public class MainActivity extends AppCompatActivity {

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;

    private boolean vsComputer = true;

    private int player1Points; //almacenar puntaje del jugador 1
    private int player2Points; //almacenar puntaje del jugador 2

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    MediaPlayer mediaPlayerPlayer1;
    MediaPlayer mediaPlayerPlayer2;

    private BoardView boardView;
    private TicTacToeGame ticTacToeGame;
    private boolean gameOver;

    private SharedPreferences sharedPreferences;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        ticTacToeGame = new TicTacToeGame();
        boardView = (BoardView) findViewById(R.id.board_ttt);
        boardView.setTicTacToeGame(ticTacToeGame);
        boardView.setOnTouchListener(boardTouchListener);

        sharedPreferences = getSharedPreferences("ttt_prefs", MODE_PRIVATE);
        player1Points = sharedPreferences.getInt("player1Points", 0);
        player2Points = sharedPreferences.getInt("player2Points", 0);
        vsComputer = sharedPreferences.getBoolean("vsComputer", true);
        gameOver = sharedPreferences.getBoolean("gameOver", false);
        updatePointsText();

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
                updatePointsText();
            }
        });
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id){
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (id) {
            case DIALOG_DIFFICULTY_ID:
                dialog = buildDifficultyDialog(builder);
                break;
            case DIALOG_QUIT_ID:
                dialog = buildExitDialog(builder);
                break;
        }
        return dialog;
    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayerPlayer1 = MediaPlayer.create(getApplicationContext(), R.raw.player_1);
        mediaPlayerPlayer2 = MediaPlayer.create(getApplicationContext(), R.raw.player_2);
    }

    /**
     *
     */
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayerPlayer1.release();
        mediaPlayerPlayer2.release();
    }

    /**
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("board", ticTacToeGame.getBoard());
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("vsComputer", vsComputer);
        outState.putBoolean("gameOver", gameOver);
        outState.putBoolean("player1Turn", ticTacToeGame.isPlayer1Turn());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ticTacToeGame.setBoard((String[][])savedInstanceState.getSerializable("board"));
        ticTacToeGame.setPlayer1Turn(savedInstanceState.getBoolean("player1Turn"));
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        vsComputer = savedInstanceState.getBoolean("vsComputer");
        gameOver = savedInstanceState.getBoolean("gameOver");
        updatePointsText();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("player1Points", player1Points);
        editor.putInt("player2Points", player2Points);
        editor.putBoolean("vsComputer", vsComputer);
        editor.putBoolean("gameOver", gameOver);
        editor.putString("difficulty", ticTacToeGame.getDifficultyLevel().name());

        editor.commit();
    }

    /**
     *
     */
    private View.OnTouchListener boardTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {

            int col = (int) event.getX() / boardView.getBoardCellWidth();
            int row = (int) event.getY() / boardView.getBoardCellHeight();

            if(ticTacToeGame.isPlayer1Turn()) {
                ticTacToeGame.setMove(TicTacToeGame.PLAYER1_MOVE, col, row);
                mediaPlayerPlayer1.start();
                setComputerMove();
            } else {
                ticTacToeGame.setMove(TicTacToeGame.PLAYER2_MOVE, col, row);
                mediaPlayerPlayer2.start();
                boardView.invalidate();
            }

            if(ticTacToeGame.checkForWin()) {
                if(ticTacToeGame.isPlayer1Turn()) {
                    player1Wins();
                } else {
                    player2Wins();
                }
                gameOver = true;
            }else if (ticTacToeGame.getRoundCount() == 9){
                draw();
                gameOver = true;
            } else {
                if(!vsComputer) {
                    ticTacToeGame.setPlayer1Turn(!ticTacToeGame.isPlayer1Turn());
                }
            }

            return false;
        }
    };


    /**
     *
     */
    private void player1Wins(){
        player1Points++;
        Toast.makeText(this, "Jugador 1 gana!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        ticTacToeGame.resetBoard();
    }

    /**
     *
     */
    private void player2Wins(){
        player2Points++;
        Toast.makeText(this, "Jugador 2 gana!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        ticTacToeGame.resetBoard();
    }

    /**
     *
     */
    private void draw(){
        Toast.makeText(this, "Empate!", Toast.LENGTH_SHORT).show();
        ticTacToeGame.resetBoard();
    }

    /**
     *
     */
    private void updatePointsText(){
        textViewPlayer1.setText("Jugador 1: " + player1Points);
        textViewPlayer2.setText("Jugador 2: " + player2Points);
    }

    /**
     *
     * @param builder
     * @return
     */
    private Dialog buildDifficultyDialog(AlertDialog.Builder builder) {
        builder.setTitle(R.string.difficulty_choose);
        final CharSequence[] levels = {
                getResources().getString(R.string.difficulty_easy),
                getResources().getString(R.string.difficulty_harder),
                getResources().getString(R.string.difficulty_expert)};
        // selected is the radio button that should be selected.
        int selected = 0;
        builder.setSingleChoiceItems(levels, selected,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        dialog.dismiss(); // Close dialog

                        if(item == 1) {
                            ticTacToeGame.setDifficultyLevel(DifficultyLevel.Harder);
                        } else if(item == 2) {
                            ticTacToeGame.setDifficultyLevel(DifficultyLevel.Expert);
                        } else {
                            ticTacToeGame.setDifficultyLevel(DifficultyLevel.Easy);
                        }

                        Toast.makeText(getApplicationContext(), levels[item],Toast.LENGTH_SHORT).show();

                    }
                });
        return builder.create();
    }

    /**
     *
     * @param builder
     * @return
     */
    private Dialog buildExitDialog(AlertDialog.Builder builder) {
        builder.setMessage(R.string.quit_question)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.no, null);
        return builder.create();
    }

    /**
     *
     */
    private void resetBoard(){
        ticTacToeGame.resetBoard();
        boardView.invalidate();
    }

    /**
     *
     */
    private void startNewGame() {
        boardView.invalidate();
        ticTacToeGame.clearBoard();
    }

    private void setComputerMove() {
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ticTacToeGame.setComputerMove();
                boardView.invalidate();
                mediaPlayerPlayer2.start();
            }
        }, 500);*/
        ticTacToeGame.setComputerMove();
        mediaPlayerPlayer2.start();
        boardView.invalidate();
    }

}