import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsonReader {
    
    public static ConnectorConfig readConfig(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filePath), ConnectorConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
