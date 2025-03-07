import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonDataReader {
    private static JsonNode jsonData;

    static {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonData = objectMapper.readTree(new File("src/test/resources/ADOConnector.json"));  // Update path if needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return jsonData.get(key).asText();
    }
     public static String getValue(String connectorType, String key) {
        return jsonData.get("connectors").get(connectorType).get(key).asText();
    }
}
