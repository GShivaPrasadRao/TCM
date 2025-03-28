package stepDefinitions;

import utils.ConfigReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ProjectSteps {
    WebDriver driver;
    String ckcUrl, ckcUsername, ckcPassword;
    String ckmUrl, ckmUsername, ckmPassword;

    public ProjectSteps() {
        ConfigReader.loadProperties(); // Load properties dynamically

        // CKC Credentials
        ckcUrl = ConfigReader.getProperty("ckc.url");
        ckcUsername = ConfigReader.getProperty("ckc.username");
        ckcPassword = ConfigReader.getProperty("ckc.password");

        // CKM Credentials
        ckmUrl = ConfigReader.getProperty("ckm.url");
        ckmUsername = ConfigReader.getProperty("ckm.username");
        ckmPassword = ConfigReader.getProperty("ckm.password");
    }

    @Given("I log in to CKC")
    public void loginToCkc() {
        driver = new ChromeDriver();
        driver.get(ckcUrl);
        // Implement login logic using ckcUsername and ckcPassword
    }

    @When("I create a project in CKC")
    public void createProjectInCkc() {
        // Implement project creation logic
    }

    @And("I log out from CKC")
    public void logoutFromCkc() {
        driver.quit();
    }

    @Given("I log in to CKM")
    public void loginToCkm() {
        driver = new ChromeDriver();
        driver.get(ckmUrl);
        // Implement login logic using ckmUsername and ckmPassword
    }

    @When("I create connectors from the above-created project in CKM")
    public void createConnectorsInCkm() {
        // Implement connector creation logic
    }

    @And("I perform History Sync in CKM")
    public void performHistorySync() {
        // Implement History Sync logic
    }

    @And("I log out from CKM")
    public void logoutFromCkm() {
        driver.quit();
    }

    @Given("I log in to CKC")
    public void reloginToCkc() {
        driver = new ChromeDriver();
        driver.get(ckcUrl);
    }

    @When("I perform Nifi Sync in CKC")
    public void performNifiSync() {
        // Implement Nifi Sync logic
    }

    @And("I log out from CKC")
    public void finalLogoutFromCkc() {
        driver.quit();
    }
}
