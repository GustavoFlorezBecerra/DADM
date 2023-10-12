package com.example.androidtictactoe.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.androidtictactoe.R;
import com.example.androidtictactoe.service.TicTacToeGame;

public class BoardView extends View {

    public static final int GRID_WIDTH = 6;

    private Bitmap playerOneBmp;
    private Bitmap playerTwoBmp;

    private Paint painter;

    private TicTacToeGame ticTacToeGame;

    public void initialize() {
        playerOneBmp = BitmapFactory.decodeResource(getResources(), R.drawable.player_one);
        playerTwoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.player_two);
        painter = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public BoardView(Context context) {
        super(context);
        initialize();
    }
    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public void setTicTacToeGame(TicTacToeGame ticTacToeGame) {
        this.ticTacToeGame = ticTacToeGame;
    }

    public int getBoardCellWidth() {
        return getWidth() / 3;
    }
    public int getBoardCellHeight() {
        return getHeight() / 3;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int boardWidth = getWidth();
        int boardHeight = getHeight();

        painter.setColor(Color.LTGRAY);
        painter.setStrokeWidth(GRID_WIDTH);

        int cellWidth = boardWidth / 3;
        canvas.drawLine(cellWidth, 0, cellWidth, boardHeight, painter);
        canvas.drawLine(cellWidth * 2, 0, cellWidth * 2, boardHeight, painter);
        canvas.drawLine(0, cellWidth, boardWidth, cellWidth, painter);
        canvas.drawLine(0, cellWidth * 2, boardWidth, cellWidth * 2, painter);

        for (int i = 0; i < TicTacToeGame.BOARD_SIZE; i++) {
            int col = i % 3;
            int row = i / 3;

            int left = col*cellWidth;
            int top = row*cellWidth;
            int right = left + cellWidth;
            int bottom = top + cellWidth;

            if (ticTacToeGame != null
                    && ticTacToeGame.getBoardOccupant(col, row).equals(TicTacToeGame.PLAYER1_MOVE)) {
                canvas.drawBitmap(playerOneBmp, null, new Rect(left, top,
                        right, bottom), null);

            }

            else if (ticTacToeGame != null
                    && ticTacToeGame.getBoardOccupant(col, row).equals(TicTacToeGame.PLAYER2_MOVE)) {
                canvas.drawBitmap(playerTwoBmp, null, new Rect(left, top,
                        right, bottom), null);
            }
        }
    }
}
