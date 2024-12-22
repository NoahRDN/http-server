package server;

import java.io.IOException;

public class ServerRunner {

    private Server server;

    public ServerRunner() {
        server = new Server();
        try {
            new Thread(() -> {
                try {
                    server.startServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        if (server != null && server.isRunning()) {
            server.stopServer();
        }
    }

    public boolean isServerRunning() {
        return server != null && server.isRunning();
    }

    public Server getServer(){
        return this.server;
    }
}
