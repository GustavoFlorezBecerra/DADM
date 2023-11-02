package com.example.androidtictactoe;

import androidx.annotation.NonNull;
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

import com.example.androidtictactoe.service.TicTacToeGame;
import com.example.androidtictactoe.util.SerializerUtil;
import com.example.androidtictactoe.view.BoardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TicTacToeActivity extends AppCompatActivity {

    static final int DIALOG_QUIT_ID = 1;


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

    private FirebaseDatabase database;
    private DatabaseReference messageRef;
    private DatabaseReference scoreRef;
    private DatabaseReference playerTurnRef;

    private String playerName = "";
    private String roomName = "";
    private String role = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        ticTacToeGame = new TicTacToeGame();
        boardView = (BoardView) findViewById(R.id.board_ttt);
        boardView.setTicTacToeGame(ticTacToeGame);

        database = FirebaseDatabase.getInstance();

        boardView.setOnTouchListener(boardTouchListener);


        sharedPreferences = getSharedPreferences("ttt_prefs", MODE_PRIVATE);
        player1Points = sharedPreferences.getInt("player1Points", 0);
        player2Points = sharedPreferences.getInt("player2Points", 0);
        gameOver = sharedPreferences.getBoolean("gameOver", false);
        playerName = sharedPreferences.getString("playerName", "");

        updatePointsText();

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            roomName = extras.getString("roomName");
            if(roomName.equals(playerName)) {
                role = "host";
            } else {
                role = "guest";
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
                updatePointsText();
            }
        });

        messageRef = database.getReference("rooms/" + roomName + "/message/board");
        messageRef.setValue(SerializerUtil.serializeBoard(ticTacToeGame.getBoard()));

        scoreRef = database.getReference("rooms/" + roomName + "/score");
        scoreRef.setValue(player1Points + "-" + player2Points);

        playerTurnRef = database.getReference("rooms/" + roomName + "/turn");
        playerTurnRef.setValue(ticTacToeGame.isPlayer1Turn());


        addRoomEventListener();
        addScoreListener();
        addPlayerTurnListener();
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
        gameOver = savedInstanceState.getBoolean("gameOver");
        updatePointsText();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("player1Points", player1Points);
        editor.putInt("player2Points", player2Points);
        editor.putBoolean("gameOver", gameOver);
        editor.putString("difficulty", ticTacToeGame.getDifficultyLevel().name());

        editor.commit();
    }

    /**
     *
     */
    private View.OnTouchListener boardTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {

            boardView.setEnabled(false);

            int col = (int) event.getX() / boardView.getBoardCellWidth();
            int row = (int) event.getY() / boardView.getBoardCellHeight();

            if(role.equals("host") && ticTacToeGame.isPlayer1Turn()) {
                ticTacToeGame.setMove(TicTacToeGame.PLAYER1_MOVE, col, row);
                mediaPlayerPlayer1.start();
                messageRef.setValue(SerializerUtil.serializeBoard(ticTacToeGame.getBoard()));
                boardView.invalidate();
                checkForWin();
            } else if(role.equals("guest") && !ticTacToeGame.isPlayer1Turn()) {
                ticTacToeGame.setMove(TicTacToeGame.PLAYER2_MOVE, col, row);
                mediaPlayerPlayer2.start();
                messageRef.setValue(SerializerUtil.serializeBoard(ticTacToeGame.getBoard()));
                boardView.invalidate();
                checkForWin();
            }
            return false;
        }
    };

    /**
     *
     */
    private void checkForWin() {

        if(ticTacToeGame.checkForWin()) {
            if(role.equals("host") && ticTacToeGame.isPlayer1Turn()) {
                player1Wins();
            } else {
                player2Wins();
            }
            gameOver = true;
        }else if (ticTacToeGame.getRoundCount() == 9){
            draw();
            gameOver = true;
        } else {
            ticTacToeGame.setPlayer1Turn(!ticTacToeGame.isPlayer1Turn());
            playerTurnRef.setValue(ticTacToeGame.isPlayer1Turn());
        }
    }
    /**
     *
     */
    private void player1Wins(){
        player1Points++;
        Toast.makeText(this, "Jugador 1 gana!", Toast.LENGTH_SHORT).show();
        ticTacToeGame.resetBoard();
        messageRef.setValue(SerializerUtil.serializeBoard(ticTacToeGame.getBoard()));
        scoreRef.setValue(player1Points + "-" + player2Points);
    }

    /**
     *
     */
    private void player2Wins(){
        player2Points++;
        Toast.makeText(TicTacToeActivity.this, "Jugador 2 gana!", Toast.LENGTH_SHORT).show();
        ticTacToeGame.resetBoard();
        messageRef.setValue(SerializerUtil.serializeBoard(ticTacToeGame.getBoard()));
        scoreRef.setValue(player1Points + "-" + player2Points);
    }

    /**
     *
     */
    private void draw(){
        Toast.makeText(TicTacToeActivity.this, "Empate!", Toast.LENGTH_SHORT).show();
        ticTacToeGame.resetBoard();
        messageRef.setValue(SerializerUtil.serializeBoard(ticTacToeGame.getBoard()));
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
    private Dialog buildExitDialog(AlertDialog.Builder builder) {
        builder.setMessage(R.string.quit_question)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TicTacToeActivity.this.finish();
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
        messageRef.setValue(SerializerUtil.serializeBoard(ticTacToeGame.getBoard()));
    }

    /**
     *
     */
    private void startNewGame() {
        ticTacToeGame.clearBoard();
        boardView.invalidate();
        messageRef.setValue(SerializerUtil.serializeBoard(ticTacToeGame.getBoard()));
    }

    /**
     * Listener para detectar cambios en el contexto de la sala de juego.
     *
     */
    private void addRoomEventListener(){
        messageRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String serializedBoard = snapshot.getValue(String.class);
                if (role.equals("host") && ticTacToeGame.isPlayer1Turn()) {
                    boardView.setEnabled(true);
                    ticTacToeGame.setBoard(SerializerUtil.deserializeBoard(serializedBoard));
                    Toast.makeText(TicTacToeActivity.this,
                            "Es turno de: " + playerName, Toast.LENGTH_SHORT).show();

                } else {
                    boardView.setEnabled(true);
                    ticTacToeGame.setBoard(SerializerUtil.deserializeBoard(serializedBoard));
                    Toast.makeText(TicTacToeActivity.this,
                            "Es turno de: " + playerName, Toast.LENGTH_SHORT).show();
                }

                boardView.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * Listener para actualizar el puntaje de juegos ganados entre los participantes.
     *
     */
    private void addScoreListener(){

        scoreRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String[] score = snapshot.getValue(String.class).split("-");
                player1Points = Integer.parseInt(score[0]);
                player2Points = Integer.parseInt(score[1]);
                updatePointsText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addPlayerTurnListener() {

        playerTurnRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isPlayer1Turn = snapshot.getValue(Boolean.class);
                ticTacToeGame.setPlayer1Turn(isPlayer1Turn);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
