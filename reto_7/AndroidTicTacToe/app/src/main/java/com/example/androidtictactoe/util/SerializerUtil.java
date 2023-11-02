package com.example.androidtictactoe.util;

import java.util.Arrays;


public class SerializerUtil {

    /**
     * Serialize the board representation into String
     * @param board
     * @return
     */
    public static String serializeBoard(String[][] board){
        StringBuilder builder = new StringBuilder();
        for(String[] row : board) {
            builder.append(Arrays.toString(row)).append(";");
        }

        return builder.toString().replace("[", "").replace("]", "");
    }

    /**
     * Deserialize the board into an array
     * @param serializedBoard
     * @return
     */
    public static String[][] deserializeBoard(String serializedBoard) {
        String[] rows = serializedBoard.split(";");

        String[][] board = new String[rows.length][rows.length];

        for(int i = 0; i < rows.length; i++) {
            String[] cols = rows[i].split(",");

            for(int j = 0; j < rows.length; j++) {
                board[i][j] = cols[j].trim();
            }
        }

        return board;
    }
}
