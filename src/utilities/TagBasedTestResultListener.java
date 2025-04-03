import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

import java.util.HashMap;
import java.util.Map;

public class TagBasedTestResultListener implements ConcurrentEventListener {

    private Map<String, TestResult> tagResults = new HashMap<>();
    private Map<String, FeatureResult> featureResults = new HashMap<>();
    private int totalTestCases = 0;
    private int totalPassed = 0;
    private int totalFailed = 0;
    private StringBuilder resultsMessage = new StringBuilder();

    private static class TestResult {
        int total = 0;
        int passed = 0;
        int failed = 0;
    }

    private static class FeatureResult {
        String featureName;
        int total = 0;
        int passed = 0;
        int failed = 0;

        public FeatureResult(String featureName) {
            this.featureName = featureName;
        }
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
        publisher.registerHandlerFor(FeatureStarted.class, this::handleFeatureStarted);
        publisher.registerHandlerFor(FeatureFinished.class, this::handleFeatureFinished);
    }

    private void handleFeatureStarted(FeatureStarted event) {
        String featureName = event.getUri().toString().replaceAll(".*/(.*).feature", "$1");
        featureResults.put(featureName, new FeatureResult(featureName));
    }

    private void handleFeatureFinished(FeatureFinished event) {
        // No specific action needed here for result aggregation
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        totalTestCases++;
        String featureName = event.getTestCase().getUri().toString().replaceAll(".*/(.*).feature", "$1");
        FeatureResult featureResult = featureResults.get(featureName);
        if (featureResult != null) {
            featureResult.total++;
        }

        for (String tag : event.getTestCase().getTags()) {
            tagResults.computeIfAbsent(tag, k -> new TestResult()).total++;
        }
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        Result.Type resultType = event.getResult().getStatus();
        if (resultType == Result.Type.PASSED) {
            totalPassed++;
            for (String tag : event.getTestCase().getTags()) {
                tagResults.get(tag).passed++;
            }
            String featureName = event.getTestCase().getUri().toString().replaceAll(".*/(.*).feature", "$1");
            FeatureResult featureResult = featureResults.get(featureName);
            if (featureResult != null) {
                featureResult.passed++;
            }
        } else if (resultType == Result.Type.FAILED) {
            totalFailed++;
            for (String tag : event.getTestCase().getTags()) {
                tagResults.get(tag).failed++;
            }
            String featureName = event.getTestCase().getUri().toString().replaceAll(".*/(.*).feature", "$1");
            FeatureResult featureResult = featureResults.get(featureName);
            if (featureResult != null) {
                featureResult.failed++;
            }
        }
        // You can handle skipped, pending, undefined if needed
    }

    private String formatResults() {
        StringBuilder sb = new StringBuilder();
        sb.append("**Test Suite Execution Results:**\n\n");
        sb.append("| Total Test Cases | Passed | Failed |\n");
        sb.append("|------------------|--------|--------|\n");
        sb.append(String.format("| %d                | %d     | %d     |\n\n", totalTestCases, totalPassed, totalFailed));

        sb.append("**Results per Feature:**\n\n");
        sb.append("| Feature Name             | Total | Passed | Failed |\n");
        sb.append("|--------------------------|-------|--------|--------|\n");
        for (FeatureResult result : featureResults.values()) {
            sb.append(String.format("| %-24s | %-5d | %-6d | %-6d |\n", result.featureName, result.total, result.passed, result.failed));
        }
        sb.append("\n");

        sb.append("**Results by Tag:**\n\n");
        sb.append("| Tag Name           | Total | Passed | Failed |\n");
        sb.append("|--------------------|-------|--------|--------|\n");
        for (Map.Entry<String, TestResult> entry : tagResults.entrySet()) {
            sb.append(String.format("| %-18s | %-5d | %-6d | %-6d |\n", entry.getKey(), entry.getValue().total, entry.getValue().passed, entry.getValue().failed));
        }
        return sb.toString();
    }

    @Override
    public void handleTestRunFinished(TestRunFinished event) {
        String formattedResults = formatResults();
        System.out.println(formattedResults); // Still print to console

        // Send to Teams here
        sendTeamsNotification(formattedResults);
    }

    private void sendTeamsNotification(String message) {
        String teamsWebhookURL = System.getProperty("teamsWebhookURL"); // Get webhook URL from system property
        if (teamsWebhookURL == null || teamsWebhookURL.isEmpty()) {
            System.err.println("Teams Webhook URL not provided. Set the 'teamsWebhookURL' system property.");
            return;
        }

        try {
            java.net.URL url = new java.net.URL(teamsWebhookURL);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonPayload = String.format("{\"text\": \"%s\"}", message.replace("\"", "\\\"").replace("\n", "<br/>")); // Format for Teams

            try (java.io.OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("Teams notification sent successfully.");
            } else {
                System.err.println("Failed to send Teams notification. Response Code: " + responseCode);
                try (java.io.InputStream errorStream = connection.getErrorStream()) {
                    if (errorStream != null) {
                        java.util.Scanner s = new java.util.Scanner(errorStream).useDelimiter("\\A");
                        String errorResponse = s.hasNext() ? s.next() : "";
                        System.err.println("Error Response: " + errorResponse);
                    }
                }
            }
            connection.disconnect();

        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.err.println("Error sending Teams notification: " + e.getMessage());
        }
    }
}