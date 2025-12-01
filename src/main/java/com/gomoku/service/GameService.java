package com.gomoku.service;

import com.gomoku.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final Board board;
    private CellState currentPlayer;
    private final AiStrategy ai;

    public GameService(Board board, AiStrategy ai) {
        this.board = board;
        this.ai = ai;
        this.currentPlayer = CellState.BLACK;
    }

    public Board getBoard() { return board; }
    public CellState getCurrentPlayer() { return currentPlayer; }

    public boolean humanMove(int r, int c) {
        if (currentPlayer != CellState.BLACK) {
            logger.warn("No es turno del humano");
            return false;
        }
        boolean ok = board.place(r,c,CellState.BLACK);
        if (ok) {
            logger.info("Humano mueve en {},{}", r, c);
            currentPlayer = CellState.WHITE;
        }
        return ok;
    }

    public Move aiMove() {
        if (currentPlayer != CellState.WHITE) {
            logger.warn("No es turno de la IA");
            return null;
        }
        Move move = ai.chooseMove(board);
        if (move == null) return null;
        boolean ok = board.place(move.row(), move.col(), CellState.WHITE);
        if (ok) {
            logger.info("IA mueve en {},{}", move.row(), move.col());
            currentPlayer = CellState.BLACK;
            return move;
        } else {
            logger.error("IA eligió movimiento inválido: {}", move);
            return null;
        }
    }

    public GameResult evaluate() {
        for (int r=0;r<board.getRows();r++) {
            for (int c=0;c<board.getCols();c++) {
                CellState s = board.get(r,c);
                if (s == CellState.EMPTY) continue;
                if (countLine(r,c,0,1,s) >= 5) return toResult(s);
                if (countLine(r,c,1,0,s) >= 5) return toResult(s);
                if (countLine(r,c,1,1,s) >= 5) return toResult(s);
                if (countLine(r,c,1,-1,s) >= 5) return toResult(s);
            }
        }
        if (board.availableMoves().isEmpty()) return GameResult.DRAW;
        return GameResult.IN_PROGRESS;
    }

    private GameResult toResult(CellState s) {
        return s == CellState.BLACK ? GameResult.BLACK_WIN : GameResult.WHITE_WIN;
    }

    public int countLine(int r, int c, int dr, int dc, CellState s) {
        int cnt = 0;
        int cr = r, cc = c;
        while (board.inBounds(cr,cc) && board.get(cr,cc) == s) {
            cnt++;
            cr += dr; cc += dc;
        }
        return cnt;
    }
}

