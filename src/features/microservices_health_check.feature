@Smoke
Feature: Microservices Health Status Check

  Scenario: Verify all microservices are UP after build
    Given I authenticate with valid credentials
    When I fetch all microservices from the discovery client
    Then all microservices should have health status as UP
    And I send the health status report to Microsoft Teams