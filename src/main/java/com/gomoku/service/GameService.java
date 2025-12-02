package com.gomoku.service;

import com.gomoku.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Game service that manages turns, places stones, and evaluates the board.
 */
public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final Board board;
    private CellState currentPlayer;
    private final AiStrategy ai;

    /**
     * Create GameService with board and ai strategy.
     * Human is BLACK and starts by default.
     * @param board board instance
     * @param ai ai strategy for WHITE
     */
    public GameService(Board board, AiStrategy ai) {
        this.board = board;
        this.ai = ai;
        this.currentPlayer = CellState.BLACK;
    }

    public Board getBoard() { return board; }
    public CellState getCurrentPlayer() { return currentPlayer; }

    /**
     * Human places a BLACK stone. Returns true if the move was placed.
     */
    public boolean humanMove(int r, int c) {
        if (currentPlayer != CellState.BLACK) {
            logger.warn("Attempted human move when not human's turn");
            return false;
        }
        boolean ok = board.place(r,c,CellState.BLACK);
        if (ok) {
            logger.info("Human moved at {},{}", r, c);
            currentPlayer = CellState.WHITE;
        }
        return ok;
    }

    /**
     * AI (WHITE) chooses and places its move. Returns the performed move or null.
     */
    public Move aiMove() {
        if (currentPlayer != CellState.WHITE) {
            logger.warn("Attempted AI move when not AI's turn");
            return null;
        }
        Move move = ai.chooseMove(board);
        if (move == null) return null;
        boolean ok = board.place(move.row(), move.col(), CellState.WHITE);
        if (ok) {
            logger.info("AI moved at {},{}", move.row(), move.col());
            currentPlayer = CellState.BLACK;
            return move;
        } else {
            logger.error("AI returned invalid move: {}", move);
            return null;
        }
    }

    /**
     * Evaluate the board for winner/draw/in-progress.
     */
    public GameResult evaluate() {
        for (int r=0;r<board.getRows();r++) {
            for (int c=0;c<board.getCols();c++) {
                CellState s = board.get(r,c);
                if (s == CellState.EMPTY) continue;
                if (countLine(r,c,0,1,s) >= 5) return s == CellState.BLACK ? GameResult.BLACK_WIN : GameResult.WHITE_WIN;
                if (countLine(r,c,1,0,s) >= 5) return s == CellState.BLACK ? GameResult.BLACK_WIN : GameResult.WHITE_WIN;
                if (countLine(r,c,1,1,s) >= 5) return s == CellState.BLACK ? GameResult.BLACK_WIN : GameResult.WHITE_WIN;
                if (countLine(r,c,1,-1,s) >= 5) return s == CellState.BLACK ? GameResult.BLACK_WIN : GameResult.WHITE_WIN;
            }
        }
        if (board.availableMoves().isEmpty()) return GameResult.DRAW;
        return GameResult.IN_PROGRESS;
    }

    /**
     * Count consecutive stones of type s starting from (r,c) in direction (dr,dc).
     * This helper counts both forwards and backwards (including the starting cell),
     * so it detects lines even when starting from an interior cell.
     */
    public int countLineBothWays(int r, int c, int dr, int dc, CellState s) {
        int count = 1; // count the starting cell
        // forward direction
        int cr = r + dr, cc = c + dc;
        while (board.inBounds(cr, cc) && board.get(cr, cc) == s) {
            count++; cr += dr; cc += dc;
        }
        // backward direction
        cr = r - dr; cc = c - dc;
        while (board.inBounds(cr, cc) && board.get(cr, cc) == s) {
            count++; cr -= dr; cc -= dc;
        }
        return count;
    }

    /**
     * Backwards-compatible method name used in tests previously:
     * keeps original behavior (count forward only).
     */
    public int countLine(int r, int c, int dr, int dc, CellState s) {
        int cnt = 0;
        int cr = r, cc = c;
        while (board.inBounds(cr,cc) && board.get(cr,cc) == s) {
            cnt++; cr += dr; cc += dc;
        }
        return cnt;
    }
}
