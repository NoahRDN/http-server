package server.model;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private String contentType;
    private byte[] body;
    private Map<String, String> headers;

    public HttpResponse(int statusCode, String contentType, byte[] body) {
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.body = body;
        this.headers = new HashMap<>();
    }

    public HttpResponse withHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public byte[] getResponseBytes() {
        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append("HTTP/1.1 ").append(statusCode).append("\r\n");
        headerBuilder.append("Content-Type: ").append(contentType).append("\r\n");
        headerBuilder.append("Content-Length: ").append(body.length).append("\r\n");

        for (Map.Entry<String, String> header : headers.entrySet()) {
            headerBuilder.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }

        headerBuilder.append("\r\n");
        byte[] headerBytes = headerBuilder.toString().getBytes();
        
        byte[] response = new byte[headerBytes.length + body.length];
        System.arraycopy(headerBytes, 0, response, 0, headerBytes.length);
        System.arraycopy(body, 0, response, headerBytes.length, body.length);
        System.out.println("------------------------------------------");
        System.out.println(headerBuilder);
        System.out.println("------------------------------------------");
        System.out.println();
        return response;
    }
}
