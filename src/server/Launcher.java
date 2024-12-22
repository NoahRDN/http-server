package server;

import java.util.Scanner;

public class Launcher {

    public static void main(String[] args) {
        ServerRunner serverRunner = new ServerRunner();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Server is running. Type 'stop' to shut it down.");
        while (true) {
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("stop")) {
                serverRunner.stopServer();
                break;
            }
        }
        scanner.close();
    }
}
