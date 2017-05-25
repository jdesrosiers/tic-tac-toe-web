package tictactoeweb.model;

import javaslang.collection.HashMap;
import javaslang.collection.List;

public class Game {
    private int id;
    private String playerX;
    private String playerO;
    private Board board;

    public Game(final String playerX, final String playerO, final Board board) {
        this.id = 1;
        this.playerX = playerX;
        this.playerO = playerO;
        this.board = board;
    }

    public int getId() {
        return id;
    }

    public String getPlayerX() {
        return playerX;
    }

    public void setPlayerX(final String playerX) {
        this.playerX = playerX;
    }

    public String getPlayerO() {
        return playerO;
    }

    public void setPlayerO(final String playerO) {
        this.playerO = playerO;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(final Board board) {
        this.board = board;
    }
}
