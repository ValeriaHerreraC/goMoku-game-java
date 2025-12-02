package com.gomoku.model;

/**
 * Represents a move on the board (row, column).
 * Internally both row and column are 0-based.
 */
public record Move(int row, int col) { }
