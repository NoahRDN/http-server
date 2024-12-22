package server;
import server.utils.Config;

import java.io.IOException;
import java.util.Scanner;

public class Launcher {

    private static Config config;
    private static ServerRunner serverRunner;

    public static void main(String[] args) {
        try {
            config = new Config();
            serverRunner = new ServerRunner();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Server is running. Type 'stop' to shut it down.");

            while (true) {
                System.out.println("Enter a command: ");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("stop")) {
                    serverRunner.stopServer();
                    break;
                } else if (command.equalsIgnoreCase("show config")) {
                    showConfig();
                } else if (command.equalsIgnoreCase("edit config")) {
                    editConfig(scanner);
                } else if (command.equalsIgnoreCase("help")) {
                    showHelp();
                } else {
                    System.out.println("Invalid command. Type 'stop' to shut down, 'edit config' to modify settings, or 'help' for command details.");
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error loading configuration: " + e.getMessage());
        }
    }

    private static void showConfig() {
        System.out.println("Current Configuration:");
        System.out.println("PHP Enabled: " + config.get("enable_php"));
        System.out.println("Public Directory: " + config.get("public_directory"));
        System.out.println("Port: " + config.get("port"));
    }

    private static void editConfig(Scanner scanner) throws IOException {
        System.out.println("Editing Configuration...");

        String phpConfig;
        while (true) {
            System.out.print("Enter new value for 'enable_php' (true/false) or press Enter to keep current: ");
            phpConfig = scanner.nextLine();
            if (phpConfig.isEmpty()) {
                break;
            } else if (phpConfig.equalsIgnoreCase("true") || phpConfig.equalsIgnoreCase("false")) {
                config.updateLineInFile("enable_php", phpConfig);
                break;
            } else {
                System.out.println("Invalid input. Please enter 'true' or 'false' only.");
            }
        }

        System.out.print("Enter new path for 'public_directory' or press Enter to keep current: ");
        String publicDir = scanner.nextLine();
        if (!publicDir.isEmpty()) {
            config.updateLineInFile("public_directory", publicDir);
        }

        String port;
        while (true) {
            System.out.print("Enter new value for 'port' (default 8080) or press Enter to keep current: ");
            port = scanner.nextLine();
            if (port.isEmpty()) {
                break;
            } else {
                try {
                    Integer.parseInt(port); 
                    config.updateLineInFile("port", port);
                    break; 
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number for the port.");
                }
            }
        }

        System.out.println("Configuration updated successfully!");

        serverRunner.stopServer();
        serverRunner = new ServerRunner(); 
    }

    private static void showHelp() {
        System.out.println("Available commands:");
        System.out.println(" - 'stop'    : Stop the server.");
        System.out.println(" - 'show config' : Display the current server configuration.");
        System.out.println(" - 'edit config' : Edit the server configuration.");
        System.out.println(" - 'help'    : Show this help message.");
    }
}


