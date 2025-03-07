Feature: ADO Connector Creation and Validation

Scenario: Verify ADO Connector Creation
Given I am on the "Projects" screen
And I navigate to Connectors in the Configuration section
When I click on "Add new Connector" button
And I select "ADO" Connector
And I enter the Tool API URL
And I enter the Tool Project
And I select the CK Project
And I enter the Username or Email ID
And I enter the Password or Access Token
And I select the Documents
And I enter the Team Name
And I enter the Area Path
And I click on "Save"
Then I should see the message "Project Connector Created Successfully"

Scenario: Verify Duplicate ADO Connector Creation is Not Allowed
Given An ADO Connector with the same details already exists
When I try to create a duplicate ADO Connector
Then I should see an error message "Connector already exists"

Scenario: Verify Connector Sync Status
Given ADO Connector is successfully created
When I check the sync status of the ADO Connector
Then the sync status should be "Success"
