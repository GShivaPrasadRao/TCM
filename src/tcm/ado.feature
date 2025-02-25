Feature: Verification of ADO Connector Creation

Scenario Outline: Create an ADO Connector with different project configurations
    Given I am on the "Projects" screen
    And I navigate to Connectors in the Configuration section
    Given I am on the "Connector Details" screen
    When I click on "Add new Connector" button on Connector Details screen
    And I select ADO Azure Connector on the dialog box from Agile Management Tools
    When I enter the text as <toolAPIURL> in Tool API URL
    And I enter the text as <toolProject> in Tool Project
    And I select the Project as <ckProject> in CK Project dropdown
    And I enter the Password or Access Token as <password>
    And I enter the Username or Email ID as <username>
    And I select the Document as <documents> in the Document Processing Algorithms dropdown
    And I select the start Date as <startDate>
    And I enter the text in Team Name as <teamName>
    And I enter the text in AreaPath as <areaPath>
    And I click on the "save" button
    Then the connector should display message as "Project Connector Created Successfully"

Examples:
    | toolAPIURL                         | toolProject           | ckProject | password        | username                  | documents       | startDate      | teamName           | areaPath                      |
    | https://dev.azure.com/Phoenix/     | Canvas Solutions      | RTPN      | 5iy988pWJ9BxN...| shivaprasad.rao@gmail.com | doc1, doc2      | February 17, 2025 | Phoenix           | Phoenix\Portfolio Team        |
    | https://dev.azure.com/Phoenix/     | New Phoenix Project  | NEW_PROJ  | NEW_PASSWORD_123 | newuser@test.com          | docA, docB      | March 5, 2025  | NewPhoenixTeam     | Phoenix\New Team Structure    |
