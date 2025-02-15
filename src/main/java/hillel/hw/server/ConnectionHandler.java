package hillel.hw.server;

import hillel.hw.commons.KeyValuePair;

import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class ConnectionHandler implements Callable<ConnectionDetails> {

    private final KeyValuePair<String, String> pair;
    private final Socket socket;

    public ConnectionHandler(final KeyValuePair<String, String> pair, final Socket socket) {
        this.socket = socket;
        this.pair = pair;
    }

    @Override
    public ConnectionDetails call() {
        return ConnectionDetails.of(pair.getValue(),
                LocalDateTime.now(), socket);
    }
}
