import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestSuiteListener implements ConcurrentEventListener {

    private static final String TEAMS_WEBHOOK_URL = "YOUR_TEAMS_WEBHOOK_URL";  // Replace with actual Teams webhook URL

    private int totalScenarios = 0;
    private int passedScenarios = 0;
    private int failedScenarios = 0;
    private final Map<String, int[]> tagCounts = new HashMap<>();
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private final EventHandler<TestRunStarted> testRunStartedHandler = event -> {
        startTime = LocalDateTime.now();  // Capture Start Time
    };

    private final EventHandler<TestCaseFinished> testCaseFinishedHandler = event -> {
        totalScenarios++;
        Status status = event.getResult().getStatus();
        boolean isPassed = status.equals(Status.PASSED);

        if (isPassed) {
            passedScenarios++;
        } else {
            failedScenarios++;
        }

        // Process Tags
        for (PickleTag tag : event.getTestCase().getTags()) {
            String tagName = tag.getName();
            tagCounts.putIfAbsent(tagName, new int[]{0, 0});
            tagCounts.get(tagName)[0]++; // Total count
            if (isPassed) {
                tagCounts.get(tagName)[1]++; // Passed count
            }
        }
    };

    private final EventHandler<TestRunFinished> testRunFinishedHandler = event -> {
        endTime = LocalDateTime.now();  // Capture End Time
    };

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestRunStarted.class, testRunStartedHandler);
        publisher.registerHandlerFor(TestCaseFinished.class, testCaseFinishedHandler);
        publisher.registerHandlerFor(TestRunFinished.class, testRunFinishedHandler);
    }

    public void sendReportToTeams() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedStartTime = startTime.format(formatter);
        String formattedEndTime = endTime.format(formatter);
        Duration executionDuration = Duration.between(startTime, endTime);
        long minutes = executionDuration.toMinutes();
        long seconds = executionDuration.toSecondsPart();
        String totalExecutionTime = String.format("%d min %d sec", minutes, seconds);

        // 1️⃣ Summary Report (Overall Stats)
        String summaryReport = """
            | Date of Execution | Total Test Scenarios | Total Passed | Total Failed | Start Time | End Time | Execution Time |
            |------------------|---------------------|-------------|-------------|------------|---------|----------------|
            | %s | %d | %d | %d | %s | %s | %s |
            """.formatted(LocalDateTime.now().toLocalDate(), totalScenarios, passedScenarios, failedScenarios, formattedStartTime, formattedEndTime, totalExecutionTime);

        // 2️⃣ Tag-Level Breakdown
        StringBuilder tagReport = new StringBuilder("""
            | Tag | Total Test Cases | Passed | Failed |
            |-----|----------------|--------|--------|
            """);
        for (Map.Entry<String, int[]> entry : tagCounts.entrySet()) {
            String tag = entry.getKey();
            int total = entry.getValue()[0];
            int passed = entry.getValue()[1];
            int failed = total - passed;
            tagReport.append("| ").append(tag).append(" | ").append(total).append(" | ").append(passed).append(" | ").append(failed).append(" |\n");
        }

        // Combine both sections
        String reportMessage = "**Test Execution Report**\n\n"
                + "**🔹 Summary**\n```\n" + summaryReport + "\n```\n"
                + "**🔹 Scenario-wise Report**\n```\n" + tagReport.toString() + "\n```\n";

        sendMessageToTeams(reportMessage);
    }

    private void sendMessageToTeams(String message) {
        try {
            URL url = new URL(TEAMS_WEBHOOK_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonPayload = "{ \"text\": \"" + message.replace("\n", "\\n") + "\" }";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200 || responseCode == 201) {
                System.out.println("✅ Report successfully sent to Microsoft Teams!");
            } else {
                System.out.println("⚠️ Failed to send report. Response Code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("⚠️ Error sending report to Microsoft Teams: " + e.getMessage());
        }
    }
}
