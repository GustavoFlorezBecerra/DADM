package com.example.androidtictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidtictactoe.service.TicTacToeEasy;
import com.example.androidtictactoe.service.TicTacToeExpert;
import com.example.androidtictactoe.service.TicTacToeHarder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String PLAYER1_MOVE = "X";
    private static final String PLAYER2_MOVE = "O";
    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;

    public enum DifficultyLevel {Easy, Harder, Expert};
    private DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;

    private Button[][] buttons = new Button[3][3]; //dimension del arreglo de botones

    private boolean player1Turn = true;
    private boolean vsComputer = true;

    private int roundCount;

    private int player1Points; //almacenar puntaje del jugador 1
    private int player2Points; //almacenar puntaje del jugador 2

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    private TicTacToeEasy ticTacToeEasy;
    private TicTacToeHarder ticTacToeHarder;
    private TicTacToeExpert ticTacToeExpert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id",getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")){
            return;
        }

        if (player1Turn){
            setMove((Button) v, PLAYER1_MOVE);
            setComputerMove();
        } else {
            setMove((Button) v, PLAYER2_MOVE);
        }

        if (checkForWin()){
            if(player1Turn){
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9){
            draw();
        } else {
            if(!vsComputer) {
                player1Turn = !player1Turn;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

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

    private boolean checkForWin(){
        String[][] field  = new String[3][3];

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        //compara jugadas por filas
        for (int i = 0; i < 3; i++){
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")){
                checkWinnerMove(field[i][0]);
                return true;
            }
        }

        for (int i = 0; i < 3; i++){
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")){
                checkWinnerMove(field[0][i]);
                return true;
            }
        }
        //compara jugadas por diagonal (de izquierda a derecha)
        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")){
            checkWinnerMove(field[0][0]);
            return true;
        }

        //compara jugadas por diagonal (de derecha a izquierda)
        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")){
            checkWinnerMove(field[0][2]);
            return true;
        }

        return false;
    }

    private void player1Wins(){
        player1Points++;
        Toast.makeText(this, "Jugador 1 gana!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void player2Wins(){
        player2Points++;
        Toast.makeText(this, "Jugador 2 gana!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void draw(){
        Toast.makeText(this, "Empate!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointsText(){
        textViewPlayer1.setText("Jugador 1: " + player1Points);
        textViewPlayer2.setText("Jugador 2: " + player2Points);
    }

    private void startNewGame(){
        player1Points = 0;
        player2Points = 0;
        resetBoard();
        updatePointsText();
    }

    private void resetBoard(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                buttons[i][j].setText("");
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void setComputerMove() {
        List<String> unoccupiedPositions = new ArrayList<>();
        for( int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(buttons[i][j].getText().toString().equals("")){
                    unoccupiedPositions.add(i +"," + j);
                }
            }
        }
        if(!unoccupiedPositions.isEmpty()) {
            String computerPosition = "";
            if(DifficultyLevel.Easy == mDifficultyLevel){
                computerPosition = calculateComputerEasyPosition();
            } else if(DifficultyLevel.Harder == mDifficultyLevel) {
                computerPosition = calculateComputerHarderPosition();
            } else {
                computerPosition = calculateComputerExpertPosition();
            }

            String[] position = computerPosition.split(",");

            setMove(buttons[Integer.parseInt(position[0])][Integer.parseInt(position[1])], PLAYER2_MOVE);
        }

        player1Turn = true;
    }



    private void setMove(Button button, String playerMove){
        button.setText(playerMove);
        roundCount++;
    }

    private void checkWinnerMove(String move) {
        if(move.equals(PLAYER2_MOVE)){
            player1Turn = false;
        }
    }

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
                            mDifficultyLevel = DifficultyLevel.Harder;
                        } else if(item == 2) {
                            mDifficultyLevel = DifficultyLevel.Expert;
                        } else {
                            mDifficultyLevel = DifficultyLevel.Easy;
                        }

                        Toast.makeText(getApplicationContext(), levels[item],Toast.LENGTH_SHORT).show();

                    }
                });
        return builder.create();
    }

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

    private String calculateComputerEasyPosition(){
        ticTacToeEasy = new TicTacToeEasy();
        return ticTacToeEasy.findBestMove(getBoardAsString());
    }

    private String calculateComputerHarderPosition(){
        ticTacToeHarder = new TicTacToeHarder();
        return ticTacToeHarder.findBestMove(getBoardAsString());
    }

    private String calculateComputerExpertPosition() {
        ticTacToeExpert = new TicTacToeExpert();
        return ticTacToeExpert.findBestMove(getBoardAsString());
    }

    private String[][] getBoardAsString() {
        String[][] board  = new String[3][3];

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                board[i][j] = buttons[i][j].getText().toString();
            }
        }

        return board;
    }

}