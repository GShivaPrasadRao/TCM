package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MulJsonDataReader {
    private static JsonNode jsonData;
    private static String environment;

    // Load the environment dynamically
    static {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/test/resources/config.properties"));
            
            // Get environment from command line or fallback to config.properties
            environment = System.getProperty("environment", properties.getProperty("default.environment", "sit")).toLowerCase();

            // Load JSON data for the given environment
            loadJsonFile(environment);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration: " + e.getMessage());
        }
    }

    private static void loadJsonFile(String env) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File("src/test/resources/json/" + env + "/connectors.json");

            if (!jsonFile.exists()) {
                throw new RuntimeException("JSON file not found for environment: " + env);
            }

            jsonData = objectMapper.readTree(jsonFile);
        } catch (IOException e) {
            throw new RuntimeException("Error loading JSON file: " + e.getMessage());
        }
    }

    // Fetch common value from JSON
    public static String getCommonValue(String key) {
        return jsonData.has(key) ? jsonData.get(key).asText() : null;
    }

    // Fetch connector-specific values
    public static String getValue(String connectorType, String key) {
        return jsonData.has("connectors") &&
               jsonData.get("connectors").has(connectorType) &&
               jsonData.get("connectors").get(connectorType).has(key)
            ? jsonData.get("connectors").get(connectorType).get(key).asText()
            : null;
    }
}
