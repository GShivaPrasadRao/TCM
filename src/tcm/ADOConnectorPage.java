import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ADOConnectorPage {

    WebDriver driver;

    @FindBy(id = "toolAPIURL")
    WebElement toolAPIURL;

    @FindBy(id = "toolProject")
    WebElement toolProject;

    @FindBy(id = "ckProject")
    WebElement ckProject;

    @FindBy(id = "password")
    WebElement password;

    @FindBy(id = "username")
    WebElement username;

    @FindBy(id = "saveButton")
    WebElement saveButton;

    @FindBy(xpath = "//div[contains(text(),'Project Connector Created Successfully')]")
    WebElement successMessage;

    public ADOConnectorPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void fillConnectorDetails(JSONObject jsonData) {
        toolAPIURL.sendKeys(jsonData.get("toolAPIURL").toString());
        toolProject.sendKeys(jsonData.get("toolProject").toString());
        ckProject.sendKeys(jsonData.get("ckProject").toString());
        password.sendKeys(jsonData.get("password").toString());
        username.sendKeys(jsonData.get("username").toString());
        saveButton.click();
    }

    public void updateConnectorDetails(JSONObject jsonData) {
        toolProject.clear();
        toolProject.sendKeys(jsonData.get("toolProject").toString());
        ckProject.clear();
        ckProject.sendKeys(jsonData.get("ckProject").toString());
        password.clear();
        password.sendKeys(jsonData.get("password").toString());
        username.clear();
        username.sendKeys(jsonData.get("username").toString());
        saveButton.click();
    }

    public boolean verifySuccessMessage(String expectedMessage) {
        return successMessage.getText().contains(expectedMessage);
    }
}
