package tictactoeweb;

import javaslang.control.Option;
import javaslang.control.Try;

class Arguments {
    static Option<Integer> getPort(final String[] args) {
        if (args.length == 0) {
            return Option.none();
        } else if (!args[0].equals("-p")) {
            throw new InvalidArgumentException("Unexpected option, [" + args[0] + "].  Expected [-p PORT]");
        } else {
            String port = Try.of(() -> args[1]).getOrElse("");
            try {
                return Option.of(Integer.valueOf(port));
            } catch (NumberFormatException nfe) {
                throw new InvalidArgumentException("Port must be an integer, [" + port + "] given");
            }
        }
    }
}
