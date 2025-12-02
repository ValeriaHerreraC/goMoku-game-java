package com.gomoku.service;

import com.gomoku.model.Board;
import com.gomoku.model.CellState;
import com.gomoku.model.Move;

import java.util.List;

/**
 * Improved AI: if it has a winning move play it; if opponent has immediate winning move block it;
 * otherwise fallback to random.
 *
 * This is simple heuristic and still fast to test.
 */
public class SmartAiStrategy implements AiStrategy {
    private final RandomAiStrategy fallback = new RandomAiStrategy();

    @Override
    public Move chooseMove(Board board) {
        List<Move> moves = board.availableMoves();
        if (moves.isEmpty()) return null;

        // Try winning move for WHITE
        for (Move m : moves) {
            Board copy = board.copy();
            copy.place(m.row(), m.col(), CellState.WHITE);
            if (isFiveInRow(copy, CellState.WHITE)) return m;
        }
        // Try to block BLACK winning move
        for (Move m : moves) {
            Board copy = board.copy();
            copy.place(m.row(), m.col(), CellState.BLACK);
            if (isFiveInRow(copy, CellState.BLACK)) return m;
        }
        // else fallback
        return fallback.chooseMove(board);
    }

    private boolean isFiveInRow(Board board, CellState s) {
        for (int r=0;r<board.getRows();r++) {
            for (int c=0;c<board.getCols();c++) {
                if (board.get(r,c) != s) continue;
                if (countLine(board, r, c, 0, 1, s) >= 5) return true;
                if (countLine(board, r, c, 1, 0, s) >= 5) return true;
                if (countLine(board, r, c, 1, 1, s) >= 5) return true;
                if (countLine(board, r, c, 1, -1, s) >= 5) return true;
            }
        }
        return false;
    }

    private int countLine(Board board, int r, int c, int dr, int dc, CellState s) {
        int cnt = 0;
        int cr = r, cc = c;
        while (board.inBounds(cr, cc) && board.get(cr,cc) == s) {
            cnt++; cr += dr; cc += dc;
        }
        return cnt;
    }
}

