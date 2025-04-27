import java.util.HashMap;
import java.util.Map;

public class HealthCheckHelper {

    public static Map<String, Map<String, String>> appServiceStatus = new HashMap<>(); // {AppName -> {ServiceName -> Status}}

    public static List<Application> loadAppConfig(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Application.class));
    }

    public static String generateAuthToken(String authUrl, String clientId, String clientSecret) {
        Response response = RestAssured.given()
            .param("client_id", clientId)
            .param("client_secret", clientSecret)
            .post(authUrl);

        return response.jsonPath().getString("access_token");
    }

    public static String getHealthStatus(String token, String healthCheckUrl) {
        Response response = RestAssured.given()
            .header("Authorization", "Bearer " + token)
            .get(healthCheckUrl);

        return response.jsonPath().getString("status");
    }

    public static void updateStatus(String appName, String serviceName, String status) {
        appServiceStatus.putIfAbsent(appName, new HashMap<>());
        appServiceStatus.get(appName).put(serviceName, status);
    }
}
