import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;

public class JsonReader {

    public JSONObject getTestData(String filePath, String testCase) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("src/test/resources/testdata/" + filePath));

            for (Object obj : jsonArray) {
                JSONObject jsonData = (JSONObject) obj;
                if (jsonData.get("testCases").equals(testCase)) {
                    return jsonData;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
