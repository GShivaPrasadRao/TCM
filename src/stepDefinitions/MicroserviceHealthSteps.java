public class MicroservicesHealthSteps {

    String token;
    Map<String, List<ServiceStatus>> projectStatusMap;

    @Given("I authenticate and retrieve the access token")
    public void authenticate() {
        token = AuthUtil.getAccessToken();
        assertNotNull(token, "Auth failed – token is null");
    }

    @When("I fetch microservices list from discovery clients")
    public void fetchMicroservices() {
        projectStatusMap = DiscoveryClientUtil.getServicesByProject(token);
        assertFalse(projectStatusMap.isEmpty(), "No services found");
    }

    @Then("I verify each service is UP")
    public void verifyServiceStatus() {
        for (Map.Entry<String, List<ServiceStatus>> entry : projectStatusMap.entrySet()) {
            for (ServiceStatus service : entry.getValue()) {
                HealthCheckUtil.checkServiceHealth(service, token);
            }
        }
    }

    @Then("I send the health status report to Microsoft Teams")
    public void sendToTeams() {
        TeamsNotifier.sendReport(projectStatusMap);
    }
}
