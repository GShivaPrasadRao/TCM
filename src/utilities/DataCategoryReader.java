import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class DataCategoryReader {
    public static void main(String[] args) throws IOException {
        String json = "{ \"testcase\":\"Nifi\", \"dataCategory\":[\"defect\",\"userstory\",\"epic\"] }";

        // Parse JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        // Read dataCategory array
        JsonNode dataCategoryNode = rootNode.get("dataCategory");

        // Iterate using a for loop
        if (dataCategoryNode.isArray()) {
            for (int i = 0; i < dataCategoryNode.size(); i++) {
                String value = dataCategoryNode.get(i).asText();
                System.out.println("Element at index " + i + ": " + value);
            }
        }
    }
}
