1. How are you getting the list of services to be monitored for availability? Is it dynamic? If not, can it be dynamic?

Currently, the list of services required is maintained in a static configuration file JSON from 
which the job reads during execution.
We can  make it dynamic by sourcing the services from the service registry.
However, some services either don’t register with the gateway — so for those, we may have to add them to the monitored list.

2. Job needs to be scheduled and find a way to trigger the job on demand from the same Teams channel?

The health check job is now scheduled at fixed intervals via Jenkins (e.g., 8 AM, 11 AM, 6 PM IST).
Will look into on-demand triggering from Teams.


3. With respect to services being automated, what all validations are currently done?

For each service fetched from the gateway’s /health endpoint .We check if the health status is UP and the response status code.



4. When can we productionalize the availability check?

The core logic for checking availability is implemented .

To productionalize, we just need to:

Finalize the service list (including those which are pending from the ML services)

Certain services are accessible without any authentication/authorization for which the Dev team is working on it.

Complete the Teams integration for on-demand execution.

Tentatively, we can be ready  within 2 weeks, depending on feedback and access/configurations for Teams and Jenkins.



✅ 1. HTTP Status Code Check
Ensure response is 200 OK or another expected status.

Mark service as down if status is 4xx/5xx.

✅ 2. Response Time (Latency) Validation
Set thresholds (e.g., warn if >1 sec, critical if >3 sec).

Helps identify slow but technically “up” services.

✅ 3. Content Validation / Payload Schema Check
Validate that expected keys/values exist in the JSON response.

For example:

json
Copy
Edit
{
  "status": "UP",
  "db": { "status": "UP" }
}
Schema-based validation can use libraries like JSON Schema Validator.

✅ 4. Dependency Check
Validate that internal dependencies (e.g., DB, Redis, external APIs) are also healthy if exposed in /health.

Actuator’s /health can show nested statuses like:

db, rabbit, elasticsearch, mail, etc.

✅ 5. Authentication Token Validity (for protected endpoints)
If services require tokens, validate:

Token is not expired

Service responds correctly to authenticated requests

✅ 6. SSL/TLS Certificate Validation
For external or public services, check for:

Valid SSL certificates (not expired)

Correct domain CN/SAN

✅ 7. DNS Resolution Check
Ensure service URLs resolve correctly to IP addresses (especially for external integrations).

Warn on DNS failures or latency.

✅ 8. Port Availability Check
(For internal services) – Try TCP connection on the service port before HTTP-level checks.

✅ 9. Expected Data/API Logic Check
If a GET API should return some count or data structure, validate content integrity.

Example: Check that /users returns a non-empty list or expected schema.

✅ 10. Retry and Stability
Retry failed checks once or twice before marking as down.

Use exponential backoff to avoid spamming failing services.

✅ 11. Alerting Thresholds (e.g., X failures in Y mins)
Don't alert on just one-time blips — alert if a service fails 3 out of 5 times or for consecutive 10 mins.

✅ 12. Version/Build Metadata Check
If services expose build/version info (/info or custom), validate expected version is deployed.

✅ 13. Database Connection Test
If not covered by actuator, manually validate DB connection/queries (e.g., SELECT 1).

✅ Bonus: Integration Health
End-to-end flow test for critical functionality (login flow, payment trigger, etc.)