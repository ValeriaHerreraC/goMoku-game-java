package com.gomoku.persistence;

import java.util.List;

/**
 * DAO for saved games.
 */
public interface SavedGameDao {
    void createTable();
    long save(int rows, int cols, String movesCsv, String result);
    List<SavedGame> listAll();
}
