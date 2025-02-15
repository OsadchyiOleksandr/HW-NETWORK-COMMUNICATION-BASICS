package hillel.hw.client;

import hillel.hw.commons.CommandType;
import hillel.hw.commons.CommonUtils;

import java.util.Scanner;

public class ClientRun {

    public static void main(String[] args) {
        Client.connectToServer();
        String clientName = CommonUtils.getClientNames().get(0);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter command exit for disconnect");
        String command = scanner.nextLine();
        while (true) {
            if (command.equalsIgnoreCase(CommandType.EXIT.getCommand())) {
                Client.sendCommandToServer("exit " + clientName);
                System.out.println("[CLIENT] Відправлено команду exit для " + clientName);
                break;
            }
            Client.sendCommandToServer(command);
            System.out.println("[CLIENT] Відправлено команду: " + command);
        }
    }
}