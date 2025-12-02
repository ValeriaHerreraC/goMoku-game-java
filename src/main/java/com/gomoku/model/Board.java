package com.gomoku.model;

import java.util.ArrayList;
import java.util.List;

/**
 * NxM Gomoku board. Provides methods to place stones and inspect board.
 */
public class Board {
    private final int rows;
    private final int cols;
    private final CellState[][] grid;

    /**
     * Create an empty board with given dimensions.
     * @param rows number of rows
     * @param cols number of columns
     */
    public Board(int rows, int cols) {
        if (rows <= 0 || cols <= 0) throw new IllegalArgumentException("rows and cols must be positive");
        this.rows = rows;
        this.cols = cols;
        this.grid = new CellState[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = CellState.EMPTY;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    /**
     * Check whether coordinates are inside board.
     */
    public boolean inBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    /**
     * Get the state of a cell.
     */
    public CellState get(int r, int c) {
        if (!inBounds(r, c)) throw new IndexOutOfBoundsException("Coordinates out of range");
        return grid[r][c];
    }

    /**
     * Place a stone at (r,c) if empty and in bounds.
     * @return true if placed successfully
     */
    public boolean place(int r, int c, CellState state) {
        if (!inBounds(r,c)) return false;
        if (grid[r][c] != CellState.EMPTY) return false;
        grid[r][c] = state;
        return true;
    }

    /**
     * Returns all empty cells as moves.
     */
    public List<Move> availableMoves() {
        List<Move> moves = new ArrayList<>();
        for (int r=0;r<rows;r++) for (int c=0;c<cols;c++) if (grid[r][c]==CellState.EMPTY) moves.add(new Move(r,c));
        return moves;
    }

    /**
     * Make a deep copy of this board.
     */
    public Board copy() {
        Board copy = new Board(rows, cols);
        for (int r=0;r<rows;r++) for (int c=0;c<cols;c++) copy.grid[r][c] = this.grid[r][c];
        return copy;
    }

    /**
     * Useful ASCII representation for CLI.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // header: columns (1-based)
        sb.append("   ");
        for (int c=0;c<cols;c++) {
            sb.append(String.format("%2d ", c+1));
        }
        sb.append("\n");
        for (int r=0;r<rows;r++) {
            sb.append(String.format("%2d ", r+1));
            for (int c=0;c<cols;c++) {
                CellState s = grid[r][c];
                char ch = switch (s) {
                    case EMPTY -> '.';
                    case BLACK -> 'X';
                    case WHITE -> 'O';
                };
                sb.append(" ").append(ch).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
