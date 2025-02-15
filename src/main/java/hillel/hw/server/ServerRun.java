package hillel.hw.server;

public class ServerRun {

    private static final int PORT = 8089;

    public static void main(String[] args) {
        Server.start(PORT);
    }
}