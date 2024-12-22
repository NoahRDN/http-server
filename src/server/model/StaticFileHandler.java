package server.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class StaticFileHandler {
    private File publicDir;
    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put(".html", "text/html");
        MIME_TYPES.put(".htm", "text/html");
        MIME_TYPES.put(".css", "text/css");
        MIME_TYPES.put(".js", "application/javascript");
        MIME_TYPES.put(".json", "application/json");
        MIME_TYPES.put(".xml", "application/xml");
        MIME_TYPES.put(".png", "image/png");
        MIME_TYPES.put(".jpg", "image/jpeg");
        MIME_TYPES.put(".jpeg", "image/jpeg");
        MIME_TYPES.put(".gif", "image/gif");
        MIME_TYPES.put(".ico", "image/x-icon");
        MIME_TYPES.put(".svg", "image/svg+xml");
        MIME_TYPES.put(".pdf", "application/pdf");
        MIME_TYPES.put(".zip", "application/zip");
        MIME_TYPES.put(".mp4", "video/mp4");
        MIME_TYPES.put(".mp3", "audio/mpeg");
        MIME_TYPES.put(".woff", "font/woff");
        MIME_TYPES.put(".woff2", "font/woff2");
    }

    private Path getFilePath(HttpRequest request) {
        String url = request.getUrl();
        Path filePath = Paths.get(publicDir.getAbsolutePath(), url.contains("?") ? url.split("\\?")[0] : url);
        return filePath.normalize();
    }

    public HttpResponse handleRequest(HttpRequest request, File publicDir) {
        this.publicDir = publicDir;
        if (request == null) {
            return new HttpResponse(400, "text/plain", "Bad Request".getBytes());
        }

        if (request.getMethod() == null || request.getUrl() == null) {
            return new HttpResponse(400, "text/plain", "Bad Request - Missing Method or URL".getBytes());
        }
        switch (request.getMethod()) {
            case "GET":
                return handleGet(request);
            case "POST":
                return handleGet(request);
            case "PUT":
                return handlePut(request);
            case "DELETE":
                return handleDelete(request);
            default:
                return new HttpResponse(405, "text/plain", "Method Not Allowed".getBytes());
        }
    }

    private HttpResponse handleGet(HttpRequest request) {
        try {
            Path filePath = getFilePath(request);
    
            if (filePath.toString().endsWith(".php")) {
                return executePhpFile(filePath.toString(), request);
            }
    
            if (Files.exists(filePath)) {
                if (Files.isDirectory(filePath)) {
                    Path indexFile = filePath.resolve("index.html");
                    Path indexFilePhp = filePath.resolve("index.php");
                    if (Files.exists(indexFile)) {
                        byte[] content = Files.readAllBytes(indexFile);
                        return new HttpResponse(200, "text/html", content);
                    } else if(Files.exists(indexFilePhp)){
                        return executePhpFile(indexFilePhp.toString(), request);
                    } else {
                        return listDirectory(filePath);
                    }
                } else {
                    byte[] content = Files.readAllBytes(filePath);
                    String mimeType = getMimeType(filePath.toString());
                    return new HttpResponse(200, mimeType, content);
                }
            } else {
                return serve404Page(filePath.toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return serve500Page();
        }
    }
    
    

    private HttpResponse handlePut(HttpRequest request) {
        try {
            Path filePath = getFilePath(request);
            Files.write(filePath, request.getBody().getBytes());
            return new HttpResponse(200, "text/plain", "File updated successfully.".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return serve500Page();
        }
    }

    private HttpResponse handleDelete(HttpRequest request) {
        try {
            Path filePath = getFilePath(request);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return new HttpResponse(200, "text/plain", "File deleted successfully.".getBytes());
            } else {
                return serve404Page(filePath.toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return serve500Page();
        }
    }

    private HttpResponse serve404Page(File filePath) {
        try {
            Path errorFilePath = Paths.get("..", "utilitaires", "errors/error404.html");
            byte[] errorContent = Files.readAllBytes(errorFilePath);
            String mimeType = getMimeType(filePath.toString());
            return new HttpResponse(404, mimeType, errorContent);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpResponse serve500Page() {
        try {
            Path errorFilePath = Paths.get("..", "utilitaires", "errors/error500.html");
            byte[] errorContent = Files.readAllBytes(errorFilePath);
            return new HttpResponse(500, "text/html", errorContent);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpResponse listDirectory(Path directory) {
        try {
            Path rootPath = Paths.get(publicDir.getAbsolutePath());
            
            Path templatePath = Paths.get("..", "utilitaires/", "directory.html");
            System.out.println(templatePath.toAbsolutePath());
            String template = Files.readString(templatePath, StandardCharsets.UTF_8);
    
            StringBuilder links = new StringBuilder();
            File[] files = directory.toFile().listFiles();
            String relativePath = rootPath.relativize(directory).toString().replace("\\", "/");
    
            if (!relativePath.isEmpty() && relativePath.lastIndexOf("/") >=-1) {
                String parentPath = "/";
                if (parentPath.isEmpty() || relativePath.lastIndexOf("/") <0) {
                    parentPath = "/"; 
                } else {
                    parentPath = relativePath.substring(0, relativePath.lastIndexOf("/"));
                    parentPath = "/" + parentPath;
                }
                links.append("<li><i class='fas fa-arrow-left'></i><a href=\"" + parentPath + "\">Retour</a></li>");
            }
            
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    String icon;
    
                    if (file.isDirectory()) {
                        icon = "<i class='fas fa-folder-open'></i>";
                    } else if (file.getName().endsWith(".html")) {
                        icon = "<i class='fas fa-file-code fa-file-code-html'></i>"; 
                    } else if (file.getName().endsWith(".css")) {
                        icon = "<i class='fas fa-file-alt'></i>"; 
                    } else if (file.getName().endsWith(".js")) {
                        icon = "<i class='fas fa-file-code fa-file-code-js'></i>"; 
                    } else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                        icon = "<i class='fas fa-file-image'></i>";
                    } else {
                        icon = "<i class='fas fa-file'></i>";
                    }
                    
                    String fullPath = relativePath.isEmpty() ? "/" + fileName : "/" + relativePath + "/" + fileName;
                    
                    if (file.isDirectory()) {
                        fullPath += "/";
                    }
    
                    String link = "<li>" + icon + "<a href=\"" + fullPath + "\">" + fileName + "</a></li>";
                    links.append(link);
                }
            }
    
            String html = template
                    .replace("{{DIRECTORY_NAME}}", "/" + relativePath)
                    .replace("{{LINKS}}", links.toString());
    
            return new HttpResponse(200, "text/html", html.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            return serve500Page();
        }
    } 

    private String getMimeType(String filePath) {
        for (String extension : MIME_TYPES.keySet()) {
            if (filePath.endsWith(extension)) {
                return MIME_TYPES.get(extension);
            }
        }
        return "application/octet-stream";
    }

    private HttpResponse executePhpFile(String requestedPath, HttpRequest request) {
        try {
            Path phpFilePath = Paths.get(requestedPath).toAbsolutePath().normalize();
    
            if (!Files.exists(phpFilePath) || !Files.isRegularFile(phpFilePath)) {
                return new HttpResponse(404, "text/plain", "File not found.".getBytes());
            }
    
            String queryString = "";
            if (request.getUrl().contains("?")) {
                queryString = request.getUrl().split("\\?", 2)[1];
            }
            System.out.println(queryString);
            ProcessBuilder processBuilder = new ProcessBuilder("php-cgi");
            processBuilder.environment().put("REDIRECT_STATUS", "1");
            processBuilder.environment().put("REQUEST_METHOD", request.getMethod());
            processBuilder.environment().put("SCRIPT_FILENAME", phpFilePath.toString());
            processBuilder.environment().put("QUERY_STRING", queryString);
            processBuilder.environment().put("CONTENT_TYPE", request.getHeader("Content-Type") != null ? request.getHeader("Content-Type") : "application/x-www-form-urlencoded");
            processBuilder.environment().put("SCRIPT_NAME", phpFilePath.getFileName().toString());
            processBuilder.environment().put("SERVER_PROTOCOL", "HTTP/1.1");
            processBuilder.environment().put("CONTENT_LENGTH", String.valueOf(request.getBody().length()));
    
            Process process = processBuilder.start();
    
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                try (OutputStream outputStream = process.getOutputStream()) {
                    outputStream.write(request.getBody().getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                }
            }
    
            String output;
            try (InputStream stdOut = process.getInputStream()) {
                output = new String(stdOut.readAllBytes(), StandardCharsets.UTF_8);
            }
    
            String errorOutput;
            try (InputStream stdErr = process.getErrorStream()) {
                errorOutput = new String(stdErr.readAllBytes(), StandardCharsets.UTF_8);
            }
    
            if (!errorOutput.isEmpty()) {
                return serve500Page();
            }
    
            String[] responseParts = output.split("\r\n\r\n", 2);
            String body = responseParts.length > 1 ? responseParts[1] : "";
    

    
            return new HttpResponse(200, "text/html", body.getBytes());
    
        } catch (IOException e) {
            e.printStackTrace();
            return serve500Page();
        }
    }
    
    
}
