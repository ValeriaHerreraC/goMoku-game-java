package com.gomoku.service;

import com.gomoku.model.Board;
import com.gomoku.model.Move;

public interface AiStrategy {
    Move chooseMove(Board board);
}

