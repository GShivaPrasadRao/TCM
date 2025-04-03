public class CucumberCustomData{ 

public static void generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("**Cucumber Test Execution Summary:**\n\n");
        sb.append("| Total Tests | Passed | Failed |\n");
        sb.append("|-------------|--------|--------|\n");
        sb.append(String.format("| %-11d | %-6d | %-6d |\n\n", totalTests, passedTests, failedTests));

        sb.append("**Tag-wise Test Execution:**\n\n");
        sb.append("| Tag Name        | Total | Passed | Failed |\n");
        sb.append("|-----------------|-------|--------|--------|\n");
        for (String tag : tagWiseCount.keySet()) {
            int total = tagWiseCount.getOrDefault(tag, 0);
            int passed = tagWisePass.getOrDefault(tag, 0);
            int failed = tagWiseFail.getOrDefault(tag, 0);
            sb.append(String.format("| %-15s | %-5d | %-6d | %-6d |\n", tag, total, passed, failed));
        }

        String report = sb.toString();
        System.out.println(report);
        sendTeamsNotification(report);
    }

rivate static void sendTeamsNotification(String message) {
        if (teamsWebhookURL == null || teamsWebhookURL.isEmpty()) {
            System.err.println("Teams Webhook URL not set. Call CucumberTestListener.setTeamsWebhookURL(\"YOUR_WEBHOOK_URL\") before generateReport().");
            return;
        }

        try {
            URL url = new URL(teamsWebhookURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonPayload = String.format("{\"text\": \"%s\"}", message.replace("\"", "\\\"").replace("\n", "<br/>"));

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                System.out.println("Teams notification sent successfully.");
            } else {
                System.err.println("Failed to send Teams notification. Response Code: " + responseCode);
                try (Scanner errorStream = new Scanner(connection.getErrorStream()).useDelimiter("\\A")) {
                    String errorResponse = errorStream.hasNext() ? errorStream.next() : "";
                    System.err.println("Error Response: " + errorResponse);
                }
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error sending Teams notification: " + e.getMessage());
        }
    }

}

