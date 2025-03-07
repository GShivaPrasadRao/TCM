import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

@When("I check the sync status of the ADO Connector")
public void CheckConenctorStatus() {
    String apiUrl = "https://api.ado.com/connectors/syncStatus?connectorName=ADO";
    
    RestAssured.baseURI = apiUrl;
    Response response = given()
            .header("Authorization", "Bearer " + accessToken)
            .when()
            .get()
            .then()
            .extract().response();

    syncStatus = response.jsonPath().getString("status");
}

@Then("the sync status should be {string}")
public void verifySyncStatusAPI(String expectedStatus) {
    assertEquals("Sync status does not match", expectedStatus, syncStatus);
}
