import org.openqa.selenium.WebDriver;
import io.cucumber.java.en.When;

public class ConnectorSteps {
    private WebDriver driver;

    public ConnectorSteps() {
        this.driver = Hooks.getDriver(); // Thread-local WebDriver
    }

    @When("I create an {string}")
    public void createConnector(String connectorType) {
        try {
            ConnectorPage connectorPage = new ConnectorPage(driver);
            connectorPage.addConnector(connectorType);
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
        }
    }
}
