public class AuthUtil {

    public static String getAccessToken() {
        Response res = RestAssured.given()
            .contentType("application/json")
            .body("{ \"username\": \"admin\", \"password\": \"admin\" }")
            .post("http://auth-service.com/auth/token");

        return res.jsonPath().getString("access_token");
    }
}
