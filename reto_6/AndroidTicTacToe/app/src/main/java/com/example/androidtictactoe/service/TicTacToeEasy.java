package com.example.androidtictactoe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToeEasy {

    public String findBestMove(String board[][]){
        List<String> unoccupiedPositions = new ArrayList<>();
        for( int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(board[i][j].equals("")){
                    unoccupiedPositions.add(i +"," + j);
                }
            }
        }

        return calculateComputerPosition(unoccupiedPositions);
    }

    private String calculateComputerPosition(List<String> unucuppiedPositions) {
        Random randomCalculator = new Random();
        return unucuppiedPositions.get(randomCalculator.nextInt(unucuppiedPositions.size()));
    }
}
