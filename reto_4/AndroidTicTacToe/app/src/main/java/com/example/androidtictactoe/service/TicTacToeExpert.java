package com.example.androidtictactoe.service;

public class TicTacToeExpert {

    private static final String PLAYER1_MOVE = "X";
    private static final String PLAYER2_MOVE = "O";

    public String findBestMove(String board[][]) {
        int bestVal = -1000;
        String bestMove = "";

        // Traverse all cells, checkWinner minimax function
        // for all empty cells. And return the position in the board
        // with optimal value.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals("")) {
                    board[i][j] = PLAYER1_MOVE;

                    int moveVal = minimax(board, 0, false);

                    board[i][j] = "";

                    if (moveVal > bestVal) {
                        bestMove = i + "," + j;
                        bestVal = moveVal;
                    }
                }
            }
        }

        return bestMove;
    }

    private int minimax(String board[][],
                        int depth, Boolean isMax) {
        int score = checkWinner(board);

        // If Maximizer has won the game
        // return evaluated score
        if (score == 10) {
            return score;
        }

        // If Minimizer has won the game
        // return evaluated score
        if (score == -10) {
            return score;
        }

        // If there are no more moves and
        // no winner then it is a tie
        if (isMovesLeft(board) == false) {
            return 0;
        }

        // If this maximizer's move
        if (isMax) {
            int best = -1000;

            // Traverse all cells
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // Check if cell is empty
                    if (board[i][j].equals("")) {
                        // Make the move
                        board[i][j] = PLAYER1_MOVE;

                        // Call minimax recursively and choose
                        // the maximum value
                        best = Math.max(best, minimax(board,
                                depth + 1, !isMax));

                        // Undo the move
                        board[i][j] = "";
                    }
                }
            }
            return best;
        } else {
            int best = 1000;

            // Traverse all cells
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // Check if cell is empty
                    if (board[i][j].equals("")) {
                        // Make the move
                        board[i][j] = PLAYER2_MOVE;

                        // Call minimax recursively and choose he minimum value
                        best = Math.min(best, minimax(board, depth + 1, !isMax));

                        // Undo the move
                        board[i][j] = "";
                    }
                }
            }
            return best;
        }
    }

    private boolean isMovesLeft(String board[][]) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals("")) {
                    return true;
                }
            }
        }
        return false;
    }

    // This is the evaluation function
    private int checkWinner(String board[][]) {
        // Checking for Rows for X or O victory.
        for (int row = 0; row < 3; row++) {
            if (board[row][0].equals(board[row][1]) && board[row][1].equals(board[row][2])) {
                if (board[row][0].equals(PLAYER1_MOVE)) {
                    return +10;
                } else if (board[row][0].equals(PLAYER2_MOVE)) {
                    return -10;
                }
            }
        }

        // Checking for Columns for X or O victory.
        for (int col = 0; col < 3; col++) {
            if (board[0][col].equals(board[1][col]) && board[1][col].equals(board[2][col])) {
                if (board[0][col].equals(PLAYER1_MOVE)) {
                    return +10;
                } else if (board[0][col].equals(PLAYER2_MOVE)) {
                    return -10;
                }
            }
        }

        // Checking for Diagonals for X or O victory.
        if (board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            if (board[0][0].equals(PLAYER1_MOVE))
                return +10;
            else if (board[0][0].equals(PLAYER2_MOVE))
                return -10;
        }

        if (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            if (board[0][2].equals(PLAYER1_MOVE))
                return +10;
            else if (board[0][2].equals(PLAYER2_MOVE))
                return -10;
        }

        // Else if none of them have won then return 0
        return 0;
    }
}
