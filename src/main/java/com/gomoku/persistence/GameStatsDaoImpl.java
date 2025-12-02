package com.gomoku.persistence;

import java.sql.*;

/**
 * H2 implementation of GameStatsDao.
 */
public class GameStatsDaoImpl implements GameStatsDao {

    @Override
    public void createTable() {
        try (Connection conn = Database.getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS game_stats (
                    id INT PRIMARY KEY CHECK (id=1),
                    games_played INT,
                    black_wins INT,
                    white_wins INT,
                    draws INT
                )
            """);
            st.executeUpdate("""
                MERGE INTO game_stats (id, games_played, black_wins, white_wins, draws)
                KEY(id) VALUES (1, 0, 0, 0, 0)
            """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameStats load() {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT games_played, black_wins, white_wins, draws FROM game_stats WHERE id = 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new GameStats(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
            } else {
                return new GameStats(0,0,0,0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(GameStats stats) {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE game_stats SET games_played=?, black_wins=?, white_wins=?, draws=? WHERE id=1")) {
            ps.setInt(1, stats.getGamesPlayed());
            ps.setInt(2, stats.getBlackWins());
            ps.setInt(3, stats.getWhiteWins());
            ps.setInt(4, stats.getDraws());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
