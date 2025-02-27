Feature: ADO Connector Functional Test Cases

  Scenario: Verification of creation of ADO Azure Connector
    Given I am on the "Projects" screen
    And I navigate to Connectors in the Configuration section
    When I click on "Add new Connector" button
    And I select "ADO Azure Connector" from Agile Management Tools
    And I enter connector details from "ADOConnectorData.json"
    Then the connector should display message "Project Connector Created Successfully"

  Scenario: Verification of Editing an Existing ADO Connector
    Given I am on the "Connectors" screen
    When I edit the "ADO Azure Connector"
    And I update the project details from "ADOConnectorData.json"
    Then the connector should display message "Connector Updated Successfully"

  Scenario: Verification of Deleting an ADO Connector
    Given I am on the "Connectors" screen
    When I delete the "ADO Azure Connector"
    Then the connector should display message "Connector Deleted Successfully"
