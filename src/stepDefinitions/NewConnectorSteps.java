import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.junit.Assert;

public class ConnectorSteps {
    private WebDriver driver;
    private JsonReader jsonReader;
    private String testCaseType;
    private String connectorType;

    public ConnectorSteps() {
        this.driver = Hooks.getDriver(); // Get WebDriver instance
    }

    @Before
    public void beforeScenario(Scenario scenario) {
       String scenarioName = scenario.getName(); // Get scenario name
        String processedName = scenarioName.replace("Verification of", "").trim(); // Remove common prefix
        System.out.println("Executing Scenario: " + processedName);

        // Extract connector type and test case from scenario name
        String[] parts = scenarioName.split(" ");
        if (parts.length >= 2) {
            connectorType = parts[0]; // First word is Connector Type
            testCaseType = parts[2].toLowerCase(); // Third word is test case type (e.g., creation, updation)
            String fileName = connectorType + "Connector.json";
            this.jsonReader = new JsonReader(fileName);
            System.out.println("Loaded JSON File: " + fileName);
        } else {
            throw new RuntimeException("Invalid scenario naming convention. Expected format: 'ConnectorType TestCaseName'");
        }
    }

    @When("I enter the Tool API URL")
    public void enterToolApiUrl() {
        WebElement toolApiUrlField = driver.findElement(By.id("toolApiUrl"));
        toolApiUrlField.sendKeys(jsonReader.getValueByTestCase(testCaseType, "toolAPIURL"));
    }

    @When("I enter the Tool Project")
    public void enterToolProject() {
        WebElement toolProjectField = driver.findElement(By.id("toolProject"));
        toolProjectField.sendKeys(jsonReader.getValueByTestCase(testCaseType, "toolProject"));
    }

    @When("I select the CK Project")
    public void selectCKProject() {
        WebElement ckProjectDropdown = driver.findElement(By.id("ckProject"));
        ckProjectDropdown.sendKeys(jsonReader.getValueByTestCase(testCaseType, "ckProject"));
    }

    @When("I enter the Username or Email ID")
    public void enterUsername() {
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(jsonReader.getValueByTestCase(testCaseType, "usernameEmailID"));
    }

    @When("I enter the Password or Access Token")
    public void enterAccessToken() {
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(jsonReader.getValueByTestCase(testCaseType, "passwordAccessToken"));
    }

    @Then("I should see the message {string}")
    public void verifySuccessMessage(String expectedMessage) {
        WebElement successMsg = driver.findElement(By.id("successMessage"));
        String actualMessage = successMsg.getText();
        Assert.assertEquals(expectedMessage, actualMessage);
    }
}
