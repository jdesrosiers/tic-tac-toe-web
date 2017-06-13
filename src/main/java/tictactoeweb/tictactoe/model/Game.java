package tictactoeweb.tictactoe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import scala.Symbol;
import tictactoe.TicTacToe;

@JsonIgnoreProperties({ "player" })
public class Game {
    private static final TicTacToe tictactoe = TicTacToe.classic();

    private int id;
    private String playerX;
    private String playerO;
    private Board board;
    private String state;

    // Required for Json Deserialization, but not used
    private Game() { }

    public Game(final String playerX, final String playerO) {
        this.id = 1;
        this.playerX = playerX;
        this.playerO = playerO;
        this.board = new Board();
        this.state = tictactoe.state(board).name();
    }

    public int getId() {
        return id;
    }

    public String getPlayerX() {
        return playerX;
    }

    public String getPlayerO() {
        return playerO;
    }

    public Board getBoard() {
        return board;
    }

    public Game play(final String position) {
        board = new Board(board.play(Symbol.apply(position)));
        state = tictactoe.state(board).name();
        return this;
    }

    public boolean canPlay(final String position) {
        return tictactoe.canPlay(Symbol.apply(position), board);
    }

    public String getState() {
        return state;
    }

    public String getPlayer() {
        return board.player().name().equals("X") ? playerX : playerO;
    }
}
