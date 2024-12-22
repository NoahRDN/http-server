package server.utils;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static final String CONFIG_FILE = "server.conf";
    private final Map<String, String> configMap = new HashMap<>();
    private final String configPath;

    public Config() throws IOException {
        String cheminRepertoireActuel = System.getProperty("user.dir");
        configPath = cheminRepertoireActuel + "/../config/" + CONFIG_FILE; 
        loadConfig();
    }

    private void loadConfig() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(configPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    configMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
    }

    public String get(String key) {
        return configMap.get(key);
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(configMap.getOrDefault(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public String[] getArray(String key) {
        String value = configMap.get(key);
        return value != null ? value.split(",") : new String[0];
    }

    public void updateLineInFile(String key, String newValue) throws IOException {
        File configFile = new File(configPath);
        File tempFile = new File(configFile.getAbsolutePath() + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(key + "=")) {
                    writer.write(key + "=" + newValue);
                    writer.newLine();
                    found = true;
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }

            if (!found) {
                writer.write(key + "=" + newValue);
                writer.newLine();
            }
        }

        if (!configFile.delete() || !tempFile.renameTo(configFile)) {
            throw new IOException("Failed to update configuration file.");
        }

        configMap.put(key, newValue);
    }
}
