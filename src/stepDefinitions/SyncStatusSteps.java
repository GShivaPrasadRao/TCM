import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

public class SyncStatusSteps {
    WebDriver driver;
    SyncStatusPage syncStatusPage;

    public SyncStatusSteps() {
        this.driver = Hooks.getDriver();
        this.syncStatusPage = new SyncStatusPage(driver);
    }

    @Then("Sync status for {string} should be {string}")
    public void sync_status_for_connector_should_be(String connectorName, String expectedStatus) {
        long startTime = System.currentTimeMillis();
        boolean isStatusVisible = syncStatusPage.isSyncStatusDisplayed(connectorName, expectedStatus);
        long endTime = System.currentTimeMillis();
        String duration = (endTime - startTime) + " ms";

        // Validate Sync Status
        boolean status = isStatusVisible;
        Assert.assertTrue("Expected sync status '" + expectedStatus + "' for connector '" + connectorName + "' is not displayed!", status);

        // Store test result
        String testStatus = status ? "✅ Passed" : "❌ Failed";
        TestResultStorage.addTestResult(connectorName, testStatus, duration);

        System.out.println("Sync validation completed for: " + connectorName);
    }
}
