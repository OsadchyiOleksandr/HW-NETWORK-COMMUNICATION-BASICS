package hillel.hw.client;

import hillel.hw.commons.CommonUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private static final String HOST = "localhost";
    private static final int PORT = 8089;

    public static void connectToServer() {
        try (var socket = new Socket(HOST, PORT);
             var socketOutputStream = socket.getOutputStream();
             var writer = new BufferedWriter(new PrintWriter(socketOutputStream))) {
            String clientName = CommonUtils.generateClientName();
            CommonUtils.getClientNames().add(clientName);
            String connectionDetails = CommonUtils.generateConnectionDetails(clientName);
            writer.write(connectionDetails);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendCommandToServer(final String command) {
        try (Socket socket = new Socket(HOST, PORT);
             OutputStream socketOutputStream = socket.getOutputStream();
             var writer = new BufferedWriter(new PrintWriter(socketOutputStream))) {
            writer.write(command);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

