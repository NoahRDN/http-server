package server.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static final String CONFIG_FILE = "server.conf";
    private final Map<String, String> configMap = new HashMap<>();

    public Config() throws IOException {
        String cheminRepertoireActuel = System.getProperty("user.dir");
        String chemin = cheminRepertoireActuel + "/../config/" ; 
        try (BufferedReader reader = new BufferedReader(new FileReader(chemin + CONFIG_FILE))) {
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
}
