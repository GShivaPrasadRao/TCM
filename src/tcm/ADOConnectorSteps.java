public class ADOConnectorSteps {

    private ADOConnectorPage adoConnectorPage;
    private JsonReader jsonReader;
    private WebDriver driver;

    public ADOConnectorSteps() {
        this.driver = WebDriverManager.getDriver();
        this.adoConnectorPage = new ADOConnectorPage(driver);
        this.jsonReader = new JsonReader();
    }

    @Given("I am on the {string} screen")
    public void iAmOnTheScreen(String screenName) {
        adoConnectorPage.navigateToScreen(screenName);
    }

    @When("I enter connector details from {string}")
    public void enterConnectorDetailsFromJson(String jsonFile) {
        JSONObject jsonData = jsonReader.getTestData(jsonFile, "Create ADO Connector");
        adoConnectorPage.fillConnectorDetails(jsonData);
    }

    @When("I update the project details from {string}")
    public void updateProjectDetails(String jsonFile) {
        JSONObject jsonData = jsonReader.getTestData(jsonFile, "Update ADO Connector");
        adoConnectorPage.updateConnectorDetails(jsonData);
    }

    @Then("the connector should display message {string}")
    public void verifySuccessMessage(String message) {
        Assert.assertTrue(adoConnectorPage.verifySuccessMessage(message));
    }
}
