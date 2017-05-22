package tictactoeweb;

import java.io.IOException;

import org.flint.Application;

public class TicTacToeWeb {
    public static void main(String[] args) throws IOException {
        Application app = new Application();

        app.run(8080);
    }
}
