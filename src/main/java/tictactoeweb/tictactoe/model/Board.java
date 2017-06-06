package tictactoeweb.tictactoe.model;

import scala.Symbol;

// JSON Deserialization requires a default constructor.  That is is the sole reason for this class
public class Board extends tictactoe.Board {
    public Board() {
        super(apply$default$1(), apply$default$2(), apply$default$3());
    }

    public Board(tictactoe.Board board) {
        super(board.player(), board.xs(), board.os());
    }
}
