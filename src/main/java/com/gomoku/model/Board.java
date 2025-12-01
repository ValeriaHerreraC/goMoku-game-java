package com.gomoku.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Tablero NxM para Gomoku. Provee métodos para colocar fichas y comprobar estado.
 */
public class Board {
    private final int rows;
    private final int cols;
    private final CellState[][] grid;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new CellState[rows][cols];
        for (int r = 0; r < rows; r++) for (int c = 0; c < cols; c++) grid[r][c] = CellState.EMPTY;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public boolean inBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public CellState get(int r, int c) {
        if (!inBounds(r,c)) throw new IndexOutOfBoundsException("Fuera del tablero");
        return grid[r][c];
    }

    public boolean place(int r, int c, CellState state) {
        if (!inBounds(r,c)) return false;
        if (grid[r][c] != CellState.EMPTY) return false;
        grid[r][c] = state;
        return true;
    }

    public List<Move> availableMoves() {
        List<Move> moves = new ArrayList<>();
        for (int r=0;r<rows;r++) for (int c=0;c<cols;c++) if (grid[r][c]==CellState.EMPTY) moves.add(new Move(r,c));
        return moves;
    }

    public Board copy() {
        Board b = new Board(rows, cols);
        for (int r=0;r<rows;r++) for (int c=0;c<cols;c++) b.grid[r][c] = this.grid[r][c];
        return b;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int r=0;r<rows;r++) {
            for (int c=0;c<cols;c++) {
                CellState s = grid[r][c];
                char ch = switch(s) {
                    case EMPTY -> '.';
                    case BLACK -> 'X';
                    case WHITE -> 'O';
                };
                sb.append(ch).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}

