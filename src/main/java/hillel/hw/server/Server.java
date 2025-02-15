package hillel.hw.server;

import hillel.hw.commons.CommandType;
import hillel.hw.commons.KeyValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static hillel.hw.commons.CommandType.NOT_FOUND;

public class Server {

    private static final List<ConnectionDetails> CONNECTION_DETAILS =
            new ArrayList<>();
    private static final ExecutorService EXECUTOR_SERVICE =
            Executors.newCachedThreadPool();

    public static void start(final int port) {
        try (var serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on " +
                    serverSocket.getInetAddress().getHostAddress() +
                    ":" + port);
            while (true) {
                var clientSocket = serverSocket.accept();
                handleCommand(clientSocket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleCommand(final Socket socket) {
        try (var socketInputStream = socket.getInputStream()) {
            try (var reader =
                         new BufferedReader(new InputStreamReader(socketInputStream))) {
                String command;
                while ((command = reader.readLine()) != null) {
                    KeyValuePair<String, String> pair =
                            getPairFromCommand(command);
                    CommandType type = Arrays.stream(CommandType.values())
                            .filter(ct -> ct.getCommand().equals(pair.getKey()))
                            .findAny()
                            .orElse(NOT_FOUND);
                    switch (type) {
                        case NAME -> registerConnection(pair, socket);
                        case EXIT -> removeConnection(pair);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerConnection(final KeyValuePair<String, String> pair, final Socket clientSocket) {
        try {
            var future = EXECUTOR_SERVICE.submit(new ConnectionHandler(pair, clientSocket));
            var connectionDetails = future.get(3, TimeUnit.SECONDS);
            if (!CONNECTION_DETAILS.contains(connectionDetails)) {
                CONNECTION_DETAILS.add(connectionDetails);
                System.out.printf("[SERVER] Client %s connected to server at %s%n",
                        connectionDetails.getClientName(), connectionDetails.getConnectedAt());
            }
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static void removeConnection(final KeyValuePair<String, String> pair) {
        boolean removed =
                CONNECTION_DETAILS.removeIf(cd ->
                        cd.getClientName().equals(pair.getValue()));
        if (removed) {
            System.out.printf("[SERVER] Removed connection for %s%n",
                    pair.getValue());
        }
    }

    private static KeyValuePair<String, String> getPairFromCommand(final String command) {
        String[] parts = command.split(" ");
        return KeyValuePair.of(parts[0], parts[1]);
    }
}
