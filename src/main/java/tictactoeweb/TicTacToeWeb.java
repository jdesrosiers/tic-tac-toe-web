package tictactoeweb;

import java.io.IOException;

import org.flint.Application;

class TicTacToeWeb {
    private static final int DEFAULT_PORT = 5000;

    public static void main(String[] args) throws IOException {
        Application app = new Application();

        app.run(Arguments.getPort(args).getOrElse(DEFAULT_PORT));
    }
}
