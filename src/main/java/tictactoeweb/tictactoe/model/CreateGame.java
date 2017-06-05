package tictactoeweb.tictactoe.model;

import tictactoe.Board;

public class CreateGame {
    private String playerX;
    private String playerO;

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

    public Game toGame() {
        return new Game(playerX, playerO);
    }
}
