import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import static org.junit.Assert.*;

public class HealthCheckSteps {

    private List<Application> applications;
    private String authToken;

    @Given("the application config file is loaded")
    public void loadApplicationConfig() {
        try {
            applications = HealthCheckHelper.loadAppConfig("path/to/your/config.json");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to load application config");
        }
    }

    @Given("I generate an auth token for {string}")
    public void generateAuthTokenForApp(String appName) {
        for (Application app : applications) {
            if (app.getAppName().equalsIgnoreCase(appName)) {
                authToken = HealthCheckHelper.generateAuthToken(app.getAuthUrl(), app.getClientId(), app.getClientSecret());
                break;
            }
        }
        assertNotNull("Auth token should not be null", authToken);
    }

    @Then("I check health for all services in {string} application")
public void checkHealthForAllServices(String appName) {
    for (Application app : applications) {
        if (app.getAppName().equalsIgnoreCase(appName)) {
            for (Service service : app.getServices()) {
                String actualStatus = HealthCheckHelper.getHealthStatus(authToken, service.getHealthCheckUrl());
                HealthCheckHelper.updateStatus(appName, service.getServiceName(), actualStatus);
                assertEquals("Health check for service " + service.getServiceName() + " failed",
                        service.getExpectedStatus(), actualStatus);
            }
        }
    }
}


    @Then("I check the overall health status of {string} application")
    public void checkOverallHealthStatus(String appName) {
        for (Application app : applications) {
            if (app.getAppName().equalsIgnoreCase(appName)) {
                String overallStatus = HealthCheckHelper.getHealthStatus(authToken, app.getServices().get(0).getHealthCheckUrl());
                assertEquals("Overall health status for " + app.getAppName() + " failed",
                        app.getExpectedStatus(), overallStatus);
            }
        }
    }

    @Then("I send the health check report to Teams and Email")
public void sendHealthCheckReport() {
    HealthCheckReportSender.sendReport(HealthCheckHelper.appServiceStatus);
}

}
