package com.gomoku.service;

import com.gomoku.model.Board;
import com.gomoku.model.Move;

import java.util.List;
import java.util.Random;

public class RandomAiStrategy implements AiStrategy {
    private final Random rng = new Random();

    @Override
    public Move chooseMove(Board board) {
        List<Move> moves = board.availableMoves();
        if (moves.isEmpty()) return null;
        return moves.get(rng.nextInt(moves.size()));
    }
}

