Feature: Health Check for Microservices

  Scenario: Check health status of services and overall health of CKBackEnd
    Given the application config file is loaded
    And I generate an auth token for "CKBackEnd"
    Then I check health for all services in "CKBackEnd" application
    And I check the overall health status of "CKBackEnd" application
