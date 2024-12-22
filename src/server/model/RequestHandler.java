package server.model;

import java.io.*;
import java.net.Socket;
import java.time.Instant;

public class RequestHandler implements Runnable {
    private Socket clientSocket;
    private File publicDir;
    private boolean enable_php;
    
    public RequestHandler(Socket clientSocket, File publicDir, boolean enable_php) {
        this.clientSocket = clientSocket;
        this.publicDir = publicDir;
        this.enable_php = enable_php;
    }
    
    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        Instant start = Instant.now();
        try (InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream()) {
                System.out.println("Input");
                // System.out.println(input.read());
            HttpRequest request = new HttpRequest(input);
            StaticFileHandler staticFileHandler = new StaticFileHandler();
            
            HttpResponse response = staticFileHandler.handleRequest(request, publicDir, enable_php);
            output.write(response.getResponseBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Instant end = Instant.now();
            System.out.println("Request processed in: "+(end.toEpochMilli() - start.toEpochMilli()) + "ms");
        }
    }
}
