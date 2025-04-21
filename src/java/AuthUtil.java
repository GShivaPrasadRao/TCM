
import io.restassured.RestAssured;
import io.restassured.response.Response;
public class AuthUtil {

    // public static String getAccessToken() {
    //     Response res = RestAssured.given()
    //         .contentType("application/json")
    //         .body("{ \"username\": \"admin\", \"password\": \"admin\" }")
    //         .post("http://auth-service.com/auth/token");

    //     return res.jsonPath().getString("access_token");
    // }



    public static String getToken() {
        try {
            Response response = RestAssured.given()
                .contentType("application/json")
                .body(/* your login payload object or JSON string */)
                .post("http://your-auth-url");

            if (response.getStatusCode() != 200) {
                System.err.println("Auth failed with status code: " + response.getStatusCode());
                return null;
            }

            String bearerToken = response.jsonPath().getString("data");

            if (bearerToken == null || bearerToken.isEmpty()) {
                System.err.println("Token not found in response.");
                return null;
            }

            if (bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7); // Remove "Bearer " prefix
            } else {
                System.out.println("Token doesn't have 'Bearer ' prefix. Returning as-is.");
                return bearerToken;
            }

        } catch (Exception e) {
            System.err.println("Exception while getting token: " + e.getMessage());
            return null;
        }
    }
}



