package tictactoeweb.model;

import javaslang.collection.List;

public class Board {
    private String player;
    private List<String> x;
    private List<String> o;

    public Board() {
        this.player = "X";
        this.x = List.empty();
        this.o = List.empty();
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public List<String> getX() {
        return x;
    }

    public void setX(final List<String> x) {
        this.x = x;
    }

    public List<String> getO() {
        return o;
    }

    public void setO(final List<String> o) {
        this.o = o;
    }
}
