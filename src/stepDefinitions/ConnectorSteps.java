import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ConnectorSteps {
    private WebDriver driver;

    public ConnectorSteps() {
        this.driver = Hooks.getDriver();  // Get driver instance from Hooks class
    }

    @When("I enter the text as {string} in Tool API URL on the Tool Configuration tab on ADO Connector")
    public void enterToolApiUrl(String key) {
        WebElement toolApiUrlField = driver.findElement(By.id("toolApiUrl"));  // Replace with actual locator
        toolApiUrlField.sendKeys(JsonDataReader.getValue(key));
    }

    @When("I enter the text as {string} in Tool Project on the Tool Configuration tab on ADO Connector")
    public void enterToolProject(String key) {
        WebElement toolProjectField = driver.findElement(By.id("toolProject"));  // Replace with actual locator
        toolProjectField.sendKeys(JsonDataReader.getValue(key));
    }

    @When("I select the Project as {string} in CK Project dropdown on the Tool Configuration tab on ADO Connector")
    public void selectCkProject(String key) {
        WebElement ckProjectDropdown = driver.findElement(By.id("ckProjectDropdown"));  // Replace with actual locator
        ckProjectDropdown.sendKeys(JsonDataReader.getValue(key));
    }

    @When("I enter the Password or Access Token as {string} on the Tool Configuration tab on ADO Connector")
    public void enterAccessToken(String key) {
        WebElement passwordField = driver.findElement(By.id("password"));  // Replace with actual locator
        passwordField.sendKeys(JsonDataReader.getValue(key));
    }

    @When("I enter the Username or Email ID as {string} on the Tool Configuration tab on ADO Connector")
    public void enterEmail(String key) {
        WebElement emailField = driver.findElement(By.id("email"));  // Replace with actual locator
        emailField.sendKeys(JsonDataReader.getValue(key));
    }

    @When("I select the Document as {string} in the Document Processing Algorithms dropdown on ADO Connector")
    public void selectDocument(String key) {
        WebElement docDropdown = driver.findElement(By.id("documentDropdown"));  // Replace with actual locator
        docDropdown.sendKeys(JsonDataReader.getValue(key));
    }

    @When("I select the start Date as {string} in the Filter tab on ADO Connector")
    public void selectStartDate(String key) {
        WebElement startDateField = driver.findElement(By.id("startDate"));  // Replace with actual locator
        startDateField.sendKeys(JsonDataReader.getValue(key));
    }

    @When("I enter the text in Team Name as {string} in the Teams & Area Path section on ADO Connector")
    public void enterTeamName(String key) {
        WebElement teamNameField = driver.findElement(By.id("teamName"));  // Replace with actual locator
        teamNameField.sendKeys(JsonDataReader.getValue(key));
    }

    @When("I enter the text in AreaPath as {string} in the Teams & Area Path section on ADO Connector")
    public void enterAreaPath(String key) {
        WebElement areaPathField = driver.findElement(By.id("areaPath"));  // Replace with actual locator
        areaPathField.sendKeys(JsonDataReader.getValue(key));
    }

    @Then("the connector should display message as {string} on ADO Connector")
    public void verifySuccessMessage(String expectedMessage) {
        WebElement successMsg = driver.findElement(By.id("successMessage"));  // Replace with actual locator
        String actualMessage = successMsg.getText();
        Assert.assertEquals(expectedMessage, actualMessage);
    }
}
