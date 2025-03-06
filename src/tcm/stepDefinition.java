import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.Map;

public class StepDefinitions {
    WebDriver driver;
    ConnectorConfig configData;

    @Given("I load the connector configuration from {string}")
    public void i_load_the_connector_configuration(String filePath) {
        configData = JsonReader.readConfig(filePath);
        System.out.println("Loaded Connector Type: " + configData.getConnectorType());
    }

    @When("I enter the text as {string} in Tool API URL on the Tool Configuration tab on ADO Connector")
    public void enter_tool_api_url(String toolAPIURL) {
        ConnectorPage connectorPage = new ConnectorPage(driver);
        connectorPage.enterToolAPIURL(configData.getToolAPIURL());
    }

    @When("I enter the text as {string} in Tool Project on the Tool Configuration tab on ADO Connector")
    public void enter_tool_project(String toolProject) {
        ConnectorPage connectorPage = new ConnectorPage(driver);
        connectorPage.enterToolProject(configData.getToolProject());
    }

    @When("I select the Project as {string} in CK Project dropdown on the Tool Configuration tab on ADO Connector")
    public void select_ck_project(String ckProject) {
        ConnectorPage connectorPage = new ConnectorPage(driver);
        connectorPage.selectCKProject(configData.getCkProject());
    }

    @When("I enter the Password or Access Token as {string} on the Tool Configuration tab on ADO Connector")
    public void enter_password_access_token(String passwordAccessToken) {
        ConnectorPage connectorPage = new ConnectorPage(driver);
        connectorPage.enterPassword(configData.getPasswordAccessToken());
    }

    @When("I enter the Username or Email ID as {string} on the Tool Configuration tab on ADO Connector")
    public void enter_username_email(String usernameEmailID) {
        ConnectorPage connectorPage = new ConnectorPage(driver);
        connectorPage.enterUsername(configData.getUsernameEmailID());
    }

    @When("I select the Document as {string} in the Document Processing Algorithms dropdown on ADO Connector")
    public void select_document(String document) {
        ConnectorPage connectorPage = new ConnectorPage(driver);
        for (String doc : configData.getDocuments()) {
            connectorPage.selectDocument(doc);
        }
    }

    @When("I select the start Date as {string} in the Filter tab on ADO Connector")
    public void select_start_date(String startDate) {
        ConnectorPage connectorPage = new ConnectorPage(driver);
        connectorPage.enterStartDate(configData.getStartDate());
    }

    @When("I enter the text in Team Name as {string} in the Teams & Area Path section on ADO Connector")
    public void enter_team_name(String teamName) {
        ConnectorPage connectorPage = new ConnectorPage(driver);
        connectorPage.enterTeamName(configData.getTeamName());
    }

    @When("I enter the text in AreaPath as {string} in the Teams & Area Path")
    public void enter_area_path(String areaPath) {
        ConnectorPage connectorPage = new ConnectorPage(driver);
        connectorPage.enterAreaPath(configData.getAreaPath());
    }

    @Then("the connector should display message as {string} on ADO Connector")
    public void verify_success_message(String expectedMessage) {
        ConnectorPage connectorPage = new ConnectorPage(driver);
        String actualMessage = connectorPage.getSuccessMessage();
        assert actualMessage.equals(expectedMessage) : "Expected message: " + expectedMessage + " but got: " + actualMessage;
    }
}
