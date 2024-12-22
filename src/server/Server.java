package server;

import server.model.RequestHandler;
import server.utils.Config;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server {
    private ServerSocket serverSocket;
    private boolean isRunning = true;
    private int port;
    private String publicDirectory;
    private boolean enable_php;
    private ExecutorService threadPool;

    public void startServer() throws IOException {
        Config config = new Config();
        port = config.getInt("port", 8080);
        publicDirectory = config.get("public_directory");
        enable_php =  Boolean.parseBoolean(config.get("enable_php"));

        threadPool = Executors.newFixedThreadPool(10);

        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (isRunning) {
            try {
                Socket clientSocket = serverSocket.accept();

                File clientDirectory = new File(publicDirectory);
   
                threadPool.execute(new RequestHandler(clientSocket, clientDirectory, enable_php));
    
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    throw e;
                }
            }
        }
    }

    public void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                isRunning = false;

                if (threadPool != null && !threadPool.isShutdown()) {
                    threadPool.shutdownNow();
                }
                System.out.println("Server stopped.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}
