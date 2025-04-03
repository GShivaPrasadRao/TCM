import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CucumberTestListener implements ConcurrentEventListener {
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;
    private static final Map<String, int[]> tagResults = new HashMap<>(); // [Total, Passed, Failed]
    private static String TEAMS_WEBHOOK_URL;

    public static void setTeamsWebhookURL(String webhookURL) {
        TEAMS_WEBHOOK_URL = webhookURL;
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::onTestCaseStart);
        publisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinish);
        publisher.registerHandlerFor(TestRunFinished.class, this::onTestRunFinish);
    }

    private void onTestCaseStart(TestCaseStarted event) {
        totalTests++;
        List<String> tags = event.getTestCase().getTags();
        updateTagResults(tags, 1, 0); // Increment total count for tags
    }

    private void onTestCaseFinish(TestCaseFinished event) {
        Result.Type status = event.getResult().getStatus();
        List<String> tags = event.getTestCase().getTags();
        if (status == Result.Type.PASSED) {
            passedTests++;
            updateTagResults(tags, 0, 1); // Increment passed count for tags
        } else if (status == Result.Type.FAILED) {
            failedTests++;
            updateTagResults(tags, 0, 0); // Increment failed count for tags (passed = 0)
        }
    }

    private void updateTagResults(List<String> tags, int totalIncrement, int passIncrement) {
        int failIncrement = (passIncrement == 0 && totalIncrement > 0) ? 1 : 0; // If total increased and not passed, then failed

        for (String tag : tags) {
            String cleanTag = tag.replace("@", "");
            tagResults.compute(cleanTag, (key, value) -> {
                if (value == null) {
                    return new int[]{totalIncrement, passIncrement, failIncrement};
                } else {
                    value[0] += totalIncrement;
                    value[1] += passIncrement;
                    value[2] += failIncrement;
                    return value;
                }
            });
        }
    }

    public static void generateReport() {
        double passPercentage = (totalTests == 0) ? 0 : (passedTests * 100.0) / totalTests;
        double failPercentage = (totalTests == 0) ? 0 : (failedTests * 100.0) / totalTests;

        String jsonPayload = buildTeamsMessage(totalTests, passedTests, failedTests, passPercentage, failPercentage, tagResults);
        sendToTeams(jsonPayload);

        // Optional: Print report to console as well
        System.out.println(jsonPayload);
    }

    private static String buildTeamsMessage(int total, int passed, int failed, double passPercentage, double failPercentage, Map<String, int[]> tagResults) {
        StringBuilder message = new StringBuilder();

        // High-Level Summary
        message.append("### 🏆 Test Execution Summary \n")
                .append("| Metric                 | Count |\n")
                .append("|------------------------|-------|\n")
                .append("| Total Test Cases       | ").append(total).append(" |\n")
                .append("| Passed Test Cases      | ").append(passed).append(" |\n")
                .append("| Failed Test Cases      | ").append(failed).append(" |\n")
                .append("| ✅ Pass % Coverage     | ").append(String.format("%.2f", passPercentage)).append("% |\n")
                .append("| ❌ Fail % Coverage     | ").append(String.format("%.2f", failPercentage)).append("% |\n\n");

        // Detailed Report by Tags
        message.append("### 📌 Detailed Test Execution by Tags \n")
                .append("| Test Suite           | Total | Passed | Failed |\n")
                .append("|----------------------|-------|--------|--------|\n");

        for (Map.Entry<String, int[]> entry : tagResults.entrySet()) {
            int[] results = entry.getValue();
            message.append("| ").append(entry.getKey()).append(" | ")
                    .append(results[0]).append(" | ")
                    .append(results[1]).append(" | ")
                    .append(results[2]).append(" |\n");
        }

        // Convert message to JSON format for Teams
        return convertToJson(message.toString());
    }

    private static String convertToJson(String markdownMessage) {
        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("text", markdownMessage);
            return new ObjectMapper().writeValueAsString(payload);
        } catch (Exception e) {
            throw new RuntimeException("Error converting message to JSON", e);
        }
    }

    private static void sendToTeams(String jsonPayload) {
        if (TEAMS_WEBHOOK_URL == null || TEAMS_WEBHOOK_URL.isEmpty()) {
            System.err.println("Teams Webhook URL not set. Call CucumberTestListener.setTeamsWebhookURL(\"YOUR_WEBHOOK_URL\") before generateReport().");
            return;
        }

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(TEAMS_WEBHOOK_URL);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(jsonPayload));

            try (CloseableHttpResponse response = client.execute(post)) {
                System.out.println("Teams Notification Response Status: " + response.getStatusLine().getStatusCode());
                // Optional: Log response body if needed
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onTestRunFinished(TestRunFinished event) {
        // generateReport will be called explicitly
    }

    public static void main(String[] args) {
        // Set your Teams Webhook URL here or via system property
        TEAMS_WEBHOOK_URL = System.getProperty("teamsWebhookURL");
        if (TEAMS_WEBHOOK_URL == null || TEAMS_WEBHOOK_URL.isEmpty()) {
            System.err.println("Warning: Teams Webhook URL not set. Notifications will not be sent.");
            // You can set a default URL here for local testing if needed
            // TEAMS_WEBHOOK_URL = "YOUR_DEFAULT_TEAMS_WEBHOOK_URL";
        }

        // Sample Test Execution Data (This part will be replaced by Cucumber events)
        int totalTestCases = 100;
        int passedTestCases = 80;
        int failedTestCases = 20;

        // Sample tag-wise breakdown (This part will be populated by Cucumber events)
        Map<String, int[]> sampleTagResults = new HashMap<>();
        sampleTagResults.put("Smoke Tests", new int[]{30, 25, 5});
        sampleTagResults.put("Regression Tests", new int[]{50, 40, 10});
        sampleTagResults.put("Performance Tests", new int[]{20, 15, 5});

        // Directly call generateReport to simulate sending the notification
        // In a real Cucumber run, this would be triggered after the tests finish.
        // You would typically call CucumberTestListener.setTeamsWebhookURL(...)
        // in a @BeforeSuite or similar setup, and CucumberTestListener.generateReport()
        // in an @AfterSuite or the onTestRunFinished event.
        CucumberTestListener.totalTests = totalTestCases;
        CucumberTestListener.passedTests = passedTestCases;
        CucumberTestListener.failedTests = failedTestCases;
        CucumberTestListener.tagResults.putAll(sampleTagResults);
        generateReport();
    }
}