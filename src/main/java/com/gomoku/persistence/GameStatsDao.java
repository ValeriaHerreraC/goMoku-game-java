package com.gomoku.persistence;

/**
 * DAO interface for GameStats.
 */
public interface GameStatsDao {
    void createTable();
    GameStats load();
    void save(GameStats stats);
}
