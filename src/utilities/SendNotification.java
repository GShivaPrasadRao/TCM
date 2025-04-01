import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TeamsNotifier {

    private static final String TEAMS_WEBHOOK_URL = "YOUR_TEAMS_WEBHOOK_URL"; // Replace with your webhook URL

    // Sends a Teams message
    private static void sendNotification(String message) {
        try {
            URL url = new URL(TEAMS_WEBHOOK_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // JSON payload for Teams message
            String payload = "{ \"text\": \"" + message + "\" }";
            byte[] output = payload.getBytes(StandardCharsets.UTF_8);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(output);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Notification sent: " + message);
            } else {
                System.err.println("Failed to send notification. Response Code: " + responseCode);
            }

        } catch (Exception e) {
            System.err.println("Error sending Teams notification: " + e.getMessage());
        }
    }

    // -------------------------- PROJECT STATUS METHODS --------------------------
    public static void notifyProjectCreated(String projectName) {
        sendNotification("🚀 **Project Created**: `" + projectName + "` successfully.");
    }

    public static void notifyProjectDeleted(String projectName) {
        sendNotification("🗑️ **Project Deleted**: `" + projectName + "` removed successfully.");
    }

    // -------------------------- CONNECTOR STATUS METHODS --------------------------
    public static void notifyConnectorCreated(String connectorName) {
        sendNotification("🔗 **Connector Created**: `" + connectorName + "` successfully.");
    }

    public static void notifyConnectorEntryVerified(String connectorName) {
        sendNotification("✅ **Connector Entry Verified**: `" + connectorName + "`.");
    }

    public static void notifyConnectorManualSyncStarted(String connectorName) {
        sendNotification("🔄 **Connector Manual Sync Started**: `" + connectorName + "`.");
    }

    public static void notifyConnectorDeleted(String connectorName) {
        sendNotification("🗑️ **Connector Deleted**: `" + connectorName + "` successfully.");
    }

    public static void notifyConnectorNifiSyncCompleted(String connectorName) {
        sendNotification("🔁 **Connector Nifi Sync Completed**: `" + connectorName + "`.");
    }

    public static void notifyConnectorDefectDataCategoryStarted(String connectorName) {
        sendNotification("📂 **Defect Data Category Sync Started**: `" + connectorName + "`.");
    }

    // -------------------------- OTHER STATUS METHODS --------------------------
    public static void notifyTestSuiteExecutionStarted() {
        sendNotification("🚀 **Test Suite Execution Started**.");
    }

    public static void notifyTestSuiteExecutionCompleted() {
        sendNotification("✅ **Test Suite Execution Completed Successfully.**");
    }

    public static void notifyError(String errorMessage) {
        sendNotification("❌ **Error Encountered**: `" + errorMessage + "`.");
    }
}
