public class CommonSteps {
    protected WebDriver driver;
    protected JsonReader jsonReader;

    public CommonSteps(String jsonFile) {
        this.driver = Hooks.getDriver();
        this.jsonReader = new JsonReader(jsonFile);
    }

    public void enterTextById(String fieldId, String jsonKey) {
        WebElement element = driver.findElement(By.id(fieldId));
        element.sendKeys(jsonReader.getValue(jsonKey));
    }
}
