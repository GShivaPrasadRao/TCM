import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;

public class TeamsNotificationSender {
    private static final String TEAMS_WEBHOOK_URL = "https://outlook.office.com/webhook/your-webhook-id";

    public static void sendTeamsNotification(String category, String connector, String connectorID, String projectName, String projectID, String syncType) {
        try {
            // JSON Payload
            String payload = "{"
                    + "\"text\": \"🔄 **Sync Status Update** \\n"
                    + "📌 *Category:* " + category + "\\n"
                    + "🔗 *Connector:* " + connector + " (" + connectorID + ") \\n"
                    + "📂 *Project:* " + projectName + " (" + projectID + ") \\n"
                    + "⚙️ *Sync Type:* " + syncType + "\\n"
                    + "✅ **Sync Completed Successfully!** \""
                    + "}";

            // Open Connection
            URL url = new URL(TEAMS_WEBHOOK_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send Request
            OutputStream os = conn.getOutputStream();
            os.write(payload.getBytes("UTF-8"));
            os.flush();
            os.close();

            // Check Response
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("✅ Notification Sent to Teams!");
            } else {
                System.out.println("❌ Failed to send notification. Response Code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        sendTeamsNotification("Agile Management", "ADO", "ADO123", "ADOConnectorProj1", "ADOCP1", "Nifi Sync");
    }
}
