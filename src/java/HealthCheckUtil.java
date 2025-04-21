public class HealthCheckUtil {

    public static void checkServiceHealth(ServiceStatus service, String token) {
        try {
            Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get(service.getUrl() + "/actuator/health");

            String status = response.jsonPath().getString("status");
            service.setStatus(status);
        } catch (Exception e) {
            service.setStatus("DOWN");
            service.setError(e.getMessage());
        }
    }
}
