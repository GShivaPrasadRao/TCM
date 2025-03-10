import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

public class TestReportNotificationSender {

    private static final String TEAMS_WEBHOOK_URL = "YOUR_TEAMS_WEBHOOK_URL";

    public static void main(String[] args) {
        // Sample Test Execution Data
        int totalTestCases = 100;
        int passedTestCases = 80;
        int failedTestCases = 20;

        // Automation Coverage Calculation
        double passPercentage = (passedTestCases * 100.0) / totalTestCases;
        double failPercentage = (failedTestCases * 100.0) / totalTestCases;

        // Sample tag-wise breakdown
        Map<String, int[]> tagResults = new HashMap<>();
        tagResults.put("Smoke Tests", new int[]{30, 25, 5});
        tagResults.put("Regression Tests", new int[]{50, 40, 10});
        tagResults.put("Performance Tests", new int[]{20, 15, 5});

        // Build Message
        String jsonPayload = buildTeamsMessage(totalTestCases, passedTestCases, failedTestCases, passPercentage, failPercentage, tagResults);

        // Send Notification
        sendToTeams(jsonPayload);
    }

    private static String buildTeamsMessage(int total, int passed, int failed, double passPercentage, double failPercentage, Map<String, int[]> tagResults) {
        StringBuilder message = new StringBuilder();

        // High-Level Summary
        message.append("### 🏆 Test Execution Summary \n")
               .append("| Metric               | Count   |\n")
               .append("|----------------------|--------|\n")
               .append("| Total Test Cases     | ").append(total).append(" |\n")
               .append("| Passed Test Cases    | ").append(passed).append(" |\n")
               .append("| Failed Test Cases    | ").append(failed).append(" |\n")
               .append("| ✅ Pass % Coverage   | ").append(String.format("%.2f", passPercentage)).append("% |\n")
               .append("| ❌ Fail % Coverage   | ").append(String.format("%.2f", failPercentage)).append("% |\n\n");

        // Detailed Report by Tags
        message.append("### 📌 Detailed Test Execution by Tags \n")
               .append("| Test Suite         | Total | Passed | Failed |\n")
               .append("|--------------------|-------|--------|--------|\n");

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
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(TEAMS_WEBHOOK_URL);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(jsonPayload));

            try (CloseableHttpResponse response = client.execute(post)) {
                System.out.println("Response Status: " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
