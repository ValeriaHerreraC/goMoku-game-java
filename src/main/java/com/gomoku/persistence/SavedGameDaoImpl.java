package com.gomoku.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * H2 implementation for saved games.
 */
public class SavedGameDaoImpl implements SavedGameDao {

    @Override
    public void createTable() {
        try (Connection conn = Database.getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS saved_games (
                  id IDENTITY PRIMARY KEY,
                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                  rows INT,
                  cols INT,
                  moves CLOB,
                  result VARCHAR(32)
                )
            """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long save(int rows, int cols, String movesCsv, String result) {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO saved_games(rows, cols, moves, result) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, rows);
            ps.setInt(2, cols);
            ps.setString(3, movesCsv);
            ps.setString(4, result);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
            return -1L;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SavedGame> listAll() {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, rows, cols, moves, result FROM saved_games ORDER BY id DESC");
             ResultSet rs = ps.executeQuery()) {
            List<SavedGame> out = new ArrayList<>();
            while (rs.next()) {
                out.add(new SavedGame(rs.getLong(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5)));
            }
            return out;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
