import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;

public class TeamsNotificationSender1 {
    private static final String TEAMS_WEBHOOK_URL = "https://outlook.office.com/webhook/your-webhook-id";  // Replace with your actual webhook URL

    public static void sendTeamsNotification(String category, String connector, String connectorID, String projectName, String projectID, String syncType, String syncStatus) {
        try {
            // Correctly formatted JSON payload with new lines
            String payload = "{ \"text\": \""
                    + "🔄 **Sync Status Update**\\n\\n"
                    + "📌 **Category:** " + category + "\\n"
                    + "🔗 **Connector:** " + connector + " (" + connectorID + ")\\n"
                    + "📂 **Project:** " + projectName + " (" + projectID + ")\\n"
                    + "⚙️ **Sync Type:** " + syncType + "\\n"
                    + "📊 **Sync Status:** " + syncStatus + "\\n\\n"
                    + getStatusEmoji(syncStatus) + " **Sync " + syncStatus + "!**\"}";

            // Open Connection
            URL url = new URL(TEAMS_WEBHOOK_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send JSON payload
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Check response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("✅ Notification Sent to Teams Successfully!");
            } else {
                System.out.println("❌ Failed to send notification. Response Code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to add emoji based on Sync Status
    private static String getStatusEmoji(String syncStatus) {
        switch (syncStatus.toLowerCase()) {
            case "completed":
                return "✅"; // Green check for success
            case "failed":
                return "❌"; // Red cross for failure
            case "partial complete":
                return "⚠️"; // Warning sign for partial success
            default:
                return "ℹ️"; // Info icon for unknown status
        }
    }

    public static void main(String[] args) {
        sendTeamsNotification("Agile Management", "ADO", "ADO123", "ADOConnectorProj1", "ADOCP1", "Nifi Sync", "Completed");
    }
}
