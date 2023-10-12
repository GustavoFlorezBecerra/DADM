package com.example.androidtictactoe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToeHarder {

    private static final String PLAYER1_MOVE = "X";

    public String findBestMove(String board[][]){
        List<String> nearUnoccupiedPositions = getAvailablePositions(board);
        if(nearUnoccupiedPositions.isEmpty()) {
            nearUnoccupiedPositions = getUnoccupiedPositions(board);
        }
        return calculateComputerPosition(nearUnoccupiedPositions);
    }

    private String calculateComputerPosition(List<String> unoccupiedPositions) {
        Random randomCalculator = new Random();
        return unoccupiedPositions.get(randomCalculator.nextInt(unoccupiedPositions.size()));
    }


    private List<String> getAvailablePositions(String[][] board) {
        List<String> availablePositions = new ArrayList<>();
        for( int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(board[i][j].equals(PLAYER1_MOVE)){
                    availablePositions = getPositions(i, j, board);
                }
            }
        }

        return availablePositions;
    }

    private List<String> getPositions(int row, int col, String[][] board) {
        List<String> positions = new ArrayList<>();
        for(int i = 0; i < row+1; i++) {
            for(int j = 0; j < col+1; j++) {
                if(board[i][j].equals("")){
                    positions.add(i+","+j);
                }
            }
        }

        return positions;
    }

    private List<String> getUnoccupiedPositions(String[][] board) {
        List<String> unoccupiedPositions = new ArrayList<>();
        for( int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(board[i][j].equals("")){
                    unoccupiedPositions.add(i +"," + j);
                }
            }
        }

        return unoccupiedPositions;
    }
}
