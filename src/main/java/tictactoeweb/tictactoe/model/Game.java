package tictactoeweb.tictactoe.model;

import tictactoe.TicTacToe;
import tictactoe.Board;

public class Game {
    private int id;
    private String playerX;
    private String playerO;
    private Board board;

    private TicTacToe tictactoe;

    public Game(final String playerX, final String playerO) {
        this.id = 1;
        this.playerX = playerX;
        this.playerO = playerO;
        this.board = Board.apply(Board.apply$default$1(), Board.apply$default$2(), Board.apply$default$3());

        this.tictactoe = TicTacToe.classic();
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

    public String getState() {
        return tictactoe.state(board).name();
    }
}
