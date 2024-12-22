package server.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String url;
    private String body;
    private Map<String, String> headers;

    public HttpRequest(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = reader.readLine();
        StringBuilder requestBody = new StringBuilder();
        boolean isBody = false;
        headers = new HashMap<>();

        if (!reader.ready()) {
            // System.out.println("WARNING: Received an empty request.");
            // throw new IllegalArgumentException("Request line is null or empty.");

        }
        
        if (line == null) {
            // throw new IllegalArgumentException("Request line is null.");
        }

        while (line != null && !line.isEmpty()) {
            if (line.startsWith("POST") || line.startsWith("PUT") || line.startsWith("DELETE") || line.startsWith("GET")) {
                String[] parts = line.split(" ");
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Invalid request line: " + line);
                }
                this.method = parts[0];
                this.url = parts[1];
            } else {
                String[] headerParts = line.split(":", 2);
                System.out.println(line);
                if (headerParts.length == 2) {
                    headers.put(headerParts[0].trim(), headerParts[1].trim());
                }
            }
            line = reader.readLine();
            if (line != null && line.isEmpty()) isBody = true; 
        }

        if (isBody) {
            while (reader.ready()) {
                requestBody.append((char) reader.read());
            }
            this.body = requestBody.toString();
        }

        if (method == null || url == null) {
            throw new IllegalArgumentException("Method or URL is null.");
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getPath() {
        return url.contains("?") ? url.split("\\?")[0] : url;
    }
}
