import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.junit.Assert;

public class ADOConnectorSteps {
    private WebDriver driver;
    private JsonReader jsonReader = new JsonReader("ADOConnector.json");

    public ADOConnectorSteps() {
        this.driver = Hooks.getDriver();
    }

    @When("I enter the Tool API URL")
    public void enterToolApiUrl() {
        WebElement toolApiUrlField = driver.findElement(By.id("toolApiUrl"));
        toolApiUrlField.sendKeys(jsonReader.getValue("toolAPIURL"));
    }

    @When("I enter the Tool Project")
    public void enterToolProject() {
        WebElement toolProjectField = driver.findElement(By.id("toolProject"));
        toolProjectField.sendKeys(jsonReader.getValue("toolProject"));
    }

    @When("I select the CK Project")
    public void selectCKProject() {
        WebElement ckProjectDropdown = driver.findElement(By.id("ckProject"));
        ckProjectDropdown.sendKeys(jsonReader.getValue("ckProject"));
    }

    @When("I enter the Username or Email ID")
    public void enterUsername() {
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(jsonReader.getValue("usernameEmailID"));
    }

    @When("I enter the Password or Access Token")
    public void enterAccessToken() {
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys(jsonReader.getValue("passwordAccessToken"));
    }

    @Then("I should see the message {string}")
    public void verifySuccessMessage(String expectedMessage) {
        WebElement successMsg = driver.findElement(By.id("successMessage"));
        String actualMessage = successMsg.getText();
        Assert.assertEquals(expectedMessage, actualMessage);
    }
}
