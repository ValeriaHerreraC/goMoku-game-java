package com.gomoku.service;

import com.gomoku.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @Test
    public void testHorizontalWinBlack() {
        Board b = new Board(5,5);
        for (int c=0;c<5;c++) b.place(2,c,CellState.BLACK);
        GameService gs = new GameService(b, new RandomAiStrategy());
        assertEquals(GameResult.BLACK_WIN, gs.evaluate());
    }

    @Test
    public void testVerticalWinWhite() {
        Board b = new Board(6,6);
        for (int r=1;r<=5;r++) b.place(r,3,CellState.WHITE);
        GameService gs = new GameService(b, new RandomAiStrategy());
        assertEquals(GameResult.WHITE_WIN, gs.evaluate());
    }

    @Test
    public void testDiagonalWin() {
        Board b = new Board(7,7);
        for (int i=0;i<5;i++) b.place(i,i,CellState.BLACK);
        GameService gs = new GameService(b, new RandomAiStrategy());
        assertEquals(GameResult.BLACK_WIN, gs.evaluate());
    }

    @Test
    public void testNoWinInProgress() {
        Board b = new Board(5,5);
        b.place(0,0,CellState.BLACK);
        b.place(0,1,CellState.WHITE);
        GameService gs = new GameService(b, new RandomAiStrategy());
        assertEquals(GameResult.IN_PROGRESS, gs.evaluate());
    }

    @Test
    public void testDraw() {
        Board b = new Board(1,1);
        b.place(0,0,CellState.BLACK);
        GameService gs = new GameService(b, new RandomAiStrategy());
        assertEquals(GameResult.DRAW, gs.evaluate());
    }

    @Test
    public void testCountLine() {
        Board b = new Board(5,5);
        b.place(0,0,CellState.BLACK);
        b.place(0,1,CellState.BLACK);
        b.place(0,2,CellState.BLACK);
        GameService gs = new GameService(b, new RandomAiStrategy());
        assertEquals(3, gs.countLine(0,0,0,1,CellState.BLACK));
    }

    @Test
    public void testHumanMoveAndAiMoveIntegration() {
        Board b = new Board(5,5);
        GameService gs = new GameService(b, new RandomAiStrategy());
        assertTrue(gs.humanMove(0,0));
        assertEquals(CellState.BLACK, b.get(0,0));
        // Now AI's turn
        Move aiMove = gs.aiMove();
        if (aiMove != null) {
            assertEquals(CellState.WHITE, b.get(aiMove.row(), aiMove.col()));
        }
    }

    @Test
    public void testInvalidMoves() {
        Board b = new Board(5,5);
        GameService gs = new GameService(b, new RandomAiStrategy());
        assertFalse(gs.humanMove(5,5)); // out of bounds
        assertTrue(gs.humanMove(0,0));
        assertFalse(gs.humanMove(0,0)); // occupied
    }
}
