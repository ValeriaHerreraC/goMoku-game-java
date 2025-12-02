package com.gomoku.persistence;

/**
 * Holds aggregate game statistics.
 */
public class GameStats {
    private int gamesPlayed;
    private int blackWins;
    private int whiteWins;
    private int draws;

    public GameStats(int gamesPlayed, int blackWins, int whiteWins, int draws) {
        this.gamesPlayed = gamesPlayed;
        this.blackWins = blackWins;
        this.whiteWins = whiteWins;
        this.draws = draws;
    }

    public int getGamesPlayed() { return gamesPlayed; }
    public int getBlackWins() { return blackWins; }
    public int getWhiteWins() { return whiteWins; }
    public int getDraws() { return draws; }

    public void incrementGamesPlayed() { gamesPlayed++; }
    public void incrementBlackWins() { blackWins++; }
    public void incrementWhiteWins() { whiteWins++; }
    public void incrementDraws() { draws++; }

    @Override
    public String toString() {
        return "Games: " + gamesPlayed + ", Black wins: " + blackWins + ", White wins: " + whiteWins + ", Draws: " + draws;
    }
}
