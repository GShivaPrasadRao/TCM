@Given("An ADO Connector with the same details already exists")
public void existingADOConnector() {
    // Check if a connector with the same name already exists
    WebElement existingConnector = driver.findElement(By.xpath("//td[contains(text(), 'ADO')]"));
    Assert.assertNotNull("Connector should already exist", existingConnector);
}

@When("I try to create a duplicate ADO Connector")
public void createDuplicateConnector() {
    WebElement addNewConnectorButton = driver.findElement(By.id("addNewConnector"));
    addNewConnectorButton.click();

    // Re-enter the same details
    enterToolApiUrl();
    enterToolProject();
    selectCKProject();
    enterUsername();
    enterAccessToken();
    selectDocuments();
    enterTeamName();
    enterAreaPath();
    
    WebElement saveButton = driver.findElement(By.id("save"));
    saveButton.click();
}

@Then("I should see an error message {string}")
public void verifyDuplicateErrorMessage(String expectedMessage) {
    WebElement errorMessage = driver.findElement(By.id("errorMessage"));
    String actualMessage = errorMessage.getText();
    Assert.assertEquals(expectedMessage, actualMessage);
}
