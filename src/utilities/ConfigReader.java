package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();

    public static void loadProperties() {
        try {
            // Load global config
            FileInputStream globalConfig = new FileInputStream("src/test/resources/config.properties");
            properties.load(globalConfig);

            // Get environment from system property or fallback to config.properties
            String env = System.getProperty("environment", properties.getProperty("default.environment", "sit")).toLowerCase();

            // Load environment-specific config
            FileInputStream envConfig = new FileInputStream("src/test/resources/config/config-" + env + ".properties");
            properties.load(envConfig);
            
        } catch (IOException e) {
            throw new RuntimeException("Configuration file not found: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
