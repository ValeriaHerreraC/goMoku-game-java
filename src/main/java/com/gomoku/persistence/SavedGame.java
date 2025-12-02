package com.gomoku.persistence;

/**
 * Represents a saved board/game in DB. Moves are stored as CSV "B:r,c;W:r,c;..."
 */
public class SavedGame {
    private final long id;
    private final int rows;
    private final int cols;
    private final String movesCsv;
    private final String result;

    public SavedGame(long id, int rows, int cols, String movesCsv, String result) {
        this.id = id;
        this.rows = rows;
        this.cols = cols;
        this.movesCsv = movesCsv;
        this.result = result;
    }

    public long getId() { return id; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public String getMovesCsv() { return movesCsv; }
    public String getResult() { return result; }

    @Override
    public String toString() {
        return "SavedGame{id=" + id + ", size=" + rows + "x" + cols + ", result=" + result + ", moves=" + movesCsv + "}";
    }
}
