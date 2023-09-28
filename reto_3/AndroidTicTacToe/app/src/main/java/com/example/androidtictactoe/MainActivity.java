package com.example.androidtictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String PLAYER1_MOVE = "X";
    private static final String PLAYER2_MOVE = "O";

    private Button[][] buttons = new Button[3][3]; //dimension del arreglo de botones

    private boolean player1Turn = true;
    private boolean vsComputer = true;

    private int roundCount;

    private int player1Points; //almacenar puntaje del jugador 1
    private int player2Points; //almacenar puntaje del jugador 2

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

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

        //roundCount++;

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
                checkWinner(field[i][0]);
                return true;
            }
        }

        for (int i = 0; i < 3; i++){
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")){
                checkWinner(field[0][i]);
                return true;
            }
        }
        //compara jugadas por diagonal (de izquierda a derecha)
        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")){
            checkWinner(field[0][0]);
            return true;
        }

        //compara jugadas por diagonal (de derecha a izquierda)
        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")){
            checkWinner(field[0][2]);
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
        List<String> unucuppiedPositions = new ArrayList<String>();
        for( int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(buttons[i][j].getText().toString().equals("")){
                    unucuppiedPositions.add(i +"," + j);
                }
            }
        }
        if(!unucuppiedPositions.isEmpty()) {
            String computerPosition = calculateComputerPosition(unucuppiedPositions);

            String[] position = computerPosition.split(",");

            setMove(buttons[Integer.parseInt(position[0])][Integer.parseInt(position[1])], PLAYER2_MOVE);
        }

        player1Turn = true;
    }

    private String calculateComputerPosition(List<String> unucuppiedPositions) {
        Random randomCalculator = new Random();
        return unucuppiedPositions.get(randomCalculator.nextInt(unucuppiedPositions.size()));
    }

    private void setMove(Button button, String playerMove){
        button.setText(playerMove);
        roundCount++;
    }

    private void checkWinner(String move) {
        if(move.equals(PLAYER2_MOVE)){
            player1Turn = false;
        }
    }

}