package com.example.androidtictactoe.service;

import com.example.androidtictactoe.enums.DifficultyLevel;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeGame {

    public static final int BOARD_SIZE = 9;
    private int roundCount;
    public static final String PLAYER1_MOVE = "X";
    public static final String PLAYER2_MOVE = "O";

    private String[][] board = new String[3][3];

    private DifficultyLevel difficultyLevel = DifficultyLevel.Easy;

    public boolean player1Turn = true;
    private boolean vsComputer = true;

    private TicTacToeEasy ticTacToeEasy;
    private TicTacToeHarder ticTacToeHarder;
    private TicTacToeExpert ticTacToeExpert;

    public TicTacToeGame(){
        clearBoard();
    }

    public int getRoundCount(){
        return this.roundCount;
    }

    public boolean isPlayer1Turn() {
        return player1Turn;
    }

    public void setPlayer1Turn(boolean player1Turn){
        this.player1Turn = player1Turn;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getBoardOccupant(int column, int row) {
        return board[column][row];
    }

    public boolean checkForWin(){

        //compara jugadas por filas
        for (int i = 0; i < 3; i++){
            if (board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]) && !board[i][0].equals("")){
                checkWinnerMove(board[i][0]);
                return true;
            }
        }

        for (int i = 0; i < 3; i++){
            if (board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]) && !board[0][i].equals("")){
                checkWinnerMove(board[0][i]);
                return true;
            }
        }
        //compara jugadas por diagonal (de izquierda a derecha)
        if (board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && !board[0][0].equals("")){
            checkWinnerMove(board[0][0]);
            return true;
        }

        //compara jugadas por diagonal (de derecha a izquierda)
        if (board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]) && !board[0][2].equals("")){
            checkWinnerMove(board[0][2]);
            return true;
        }

        return false;
    }

    public void clearBoard(){
        for( int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                board[i][j] = "";
            }
        }
    }

    public void setMove(String playerMove, int column, int row){
        board[column][row] = playerMove;
        roundCount++;
    }

    private void checkWinnerMove(String move) {
        if(move.equals(PLAYER2_MOVE)){
            player1Turn = false;
        }
    }

    public void resetBoard(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                board[i][j] = "";
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    public void setComputerMove() {
        List<String> unoccupiedPositions = new ArrayList<>();
        for( int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(board[i][j].equals("")){
                    unoccupiedPositions.add(i +"," + j);
                }
            }
        }
        if(!unoccupiedPositions.isEmpty()) {
            String computerPosition = "";
            if(DifficultyLevel.Easy == difficultyLevel){
                computerPosition = calculateComputerEasyPosition();
            } else if(DifficultyLevel.Harder == difficultyLevel) {
                computerPosition = calculateComputerHarderPosition();
            } else {
                computerPosition = calculateComputerExpertPosition();
            }

            String[] position = computerPosition.split(",");

            setMove(PLAYER2_MOVE, Integer.parseInt(position[0]), Integer.parseInt(position[1]));
        }

        player1Turn = true;
    }

    private String calculateComputerEasyPosition(){
        ticTacToeEasy = new TicTacToeEasy();
        return ticTacToeEasy.findBestMove(board);
    }

    private String calculateComputerHarderPosition(){
        ticTacToeHarder = new TicTacToeHarder();
        return ticTacToeHarder.findBestMove(board);
    }

    private String calculateComputerExpertPosition() {
        ticTacToeExpert = new TicTacToeExpert();
        return ticTacToeExpert.findBestMove(board);
    }
}
