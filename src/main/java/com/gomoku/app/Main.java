package com.gomoku.app;

import com.gomoku.model.*;
import com.gomoku.persistence.*;
import com.gomoku.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * Command-line Gomoku. Human (BLACK) vs AI (WHITE).
 *
 * Commands:
 *  - show
 *  - move r c          (1-based coordinates)
 *  - ai                (force AI to play)
 *  - stats             (show aggregate stats)
 *  - save              (save current board to DB)
 *  - saved             (list saved games)
 *  - help
 *  - exit
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        GameStatsDao statsDao = new GameStatsDaoImpl();
        statsDao.createTable();
        GameStats stats = statsDao.load();

        SavedGameDao savedGameDao = new SavedGameDaoImpl();
        savedGameDao.createTable();

        Scanner sc = new Scanner(System.in);
        Board board = new Board(15,15);
        // Use Smart AI (better than random)
        GameService gs = new GameService(board, new SmartAiStrategy());

        // Track moves as strings "B:r,c;W:r,c;..."
        StringJoiner movesJoiner = new StringJoiner(";");

        System.out.println("Gomoku CLI - 15x15. Commands: show, move r c, ai, save, saved, stats, help, exit");

        while (true) {
            System.out.print("> ");
            String line = sc.nextLine().trim();
            if (line.isBlank()) continue;
            String[] parts = line.split("\\s+");
            String cmd = parts[0].toLowerCase();

            switch (cmd) {
                case "help" -> System.out.println("Commands: show | move r c | ai | save | saved | stats | help | exit");

                case "show" -> System.out.println(board);

                case "stats" -> System.out.println(stats);

                case "saved" -> {
                    List<SavedGame> list = savedGameDao.listAll();
                    if (list.isEmpty()) System.out.println("No saved games");
                    else list.forEach(g -> System.out.println(g));
                }

                case "save" -> {
                    String movesCsv = movesJoiner.length() == 0 ? "" : movesJoiner.toString();
                    GameResult res = gs.evaluate();
                    long id = savedGameDao.save(board.getRows(), board.getCols(), movesCsv, res.name());
                    System.out.println("Saved game id: " + id);
                    logger.info("Saved game {} result={} moves={}", id, res, movesCsv);
                }

                case "move" -> {
                    if (parts.length < 3) {
                        System.out.println("Usage: move r c");
                        break;
                    }
                    try {
                        int r = Integer.parseInt(parts[1]) - 1;
                        int c = Integer.parseInt(parts[2]) - 1;

                        if (gs.humanMove(r,c)) {
                            movesJoiner.add("B:" + (r+1) + "," + (c+1)); // store 1-based for readability
                            System.out.println("Move accepted\n" + board);
                            logger.info("Human moved {} {}", r, c);

                            GameResult res = gs.evaluate();
                            if (res != GameResult.IN_PROGRESS) {
                                System.out.println("Game over: " + res);
                                saveStatsOnEnd(statsDao, stats, res);
                                logger.info("Game ended: {}", res);
                                continue;
                            }

                            gs.aiMove();
                            // find last AI move by scanning board (not ideal but fine for CLI)
                            Move lastAi = findLastMove(board, CellState.WHITE);
                            if (lastAi != null) movesJoiner.add("W:" + (lastAi.row()+1) + "," + (lastAi.col()+1));

                            System.out.println("AI moved\n" + board);
                            logger.info("AI moved");

                            res = gs.evaluate();
                            if (res != GameResult.IN_PROGRESS) {
                                System.out.println("Game over: " + res);
                                saveStatsOnEnd(statsDao, stats, res);
                                logger.info("Game ended: {}", res);
                            }
                        } else {
                            System.out.println("Invalid move");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Row and column must be numbers");
                    }
                }

                case "ai" -> {
                    gs.aiMove();
                    Move lastAi = findLastMove(board, CellState.WHITE);
                    if (lastAi != null) System.out.println("AI moved at " + (lastAi.row()+1) + "," + (lastAi.col()+1));
                    System.out.println(board);
                    GameResult res = gs.evaluate();
                    if (res != GameResult.IN_PROGRESS) {
                        System.out.println("Game over: " + res);
                        saveStatsOnEnd(statsDao, stats, res);
                        logger.info("Game ended: {}", res);
                    }
                }

                case "exit" -> {
                    System.out.println("Goodbye!");
                    return;
                }

                default -> System.out.println("Unknown command. Type 'help' for assistance.");
            }
        }
    }

    private static void saveStatsOnEnd(GameStatsDao statsDao, GameStats stats, GameResult res) {
        stats.incrementGamesPlayed();
        switch (res) {
            case BLACK_WIN -> stats.incrementBlackWins();
            case WHITE_WIN -> stats.incrementWhiteWins();
            case DRAW -> stats.incrementDraws();
            default -> {}
        }
        statsDao.save(stats);
        System.out.println("Saved stats: " + stats);
    }

    private static Move findLastMove(Board board, CellState s) {
        for (int r=board.getRows()-1;r>=0;r--) {
            for (int c=board.getCols()-1;c>=0;c--) {
                if (board.get(r,c) == s) return new Move(r,c);
            }
        }
        return null;
    }
}
