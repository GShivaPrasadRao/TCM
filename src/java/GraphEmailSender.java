package java;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class GraphEmailSender {

    // Replace with your values
    private static final String CLIENT_ID = "your-client-id";
    private static final String CLIENT_SECRET = "your-client-secret";
    private static final String TENANT_ID = "your-tenant-id";
    private static final String FROM_EMAIL = "your-email@domain.com";
    private static final String TO_EMAIL = "recipient@domain.com";
    private static final String REPORT_PATH = "newman-report.html";

    public static void main(String[] args) throws Exception {
        String accessToken = getAccessToken();
        sendEmail(accessToken);
    }

    private static String getAccessToken() throws IOException {
        URL url = new URL("https://login.microsoftonline.com/" + TENANT_ID + "/oauth2/v2.0/token");
        String params = "client_id=" + CLIENT_ID +
                "&scope=https%3A%2F%2Fgraph.microsoft.com%2F.default" +
                "&client_secret=" + CLIENT_SECRET +
                "&grant_type=client_credentials";

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes());
        }

        String response = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                .lines().collect(Collectors.joining("\n"));

        JSONObject json = new JSONObject(response);
        return json.getString("access_token");
    }

    private static void sendEmail(String accessToken) throws Exception {
        byte[] fileContent = Files.readAllBytes(new File(REPORT_PATH).toPath());
        String encoded = Base64.getEncoder().encodeToString(fileContent);

        JSONObject message = new JSONObject();
        message.put("subject", "Newman Test Report");
        JSONObject body = new JSONObject();
        body.put("contentType", "HTML");
        body.put("content", "Hi Team,<br><br>Attached is the Newman test report.<br><br>Regards,<br>Jenkins");
        message.put("body", body);

        JSONArray toRecipients = new JSONArray();
        JSONObject toEmail = new JSONObject();
        toEmail.put("address", TO_EMAIL);
        JSONObject emailAddress = new JSONObject();
        emailAddress.put("emailAddress", toEmail);
        toRecipients.put(emailAddress);
        message.put("toRecipients", toRecipients);

        JSONArray attachments = new JSONArray();
        JSONObject attachment = new JSONObject();
        attachment.put("@odata.type", "#microsoft.graph.fileAttachment");
        attachment.put("name", "newman-report.html");
        attachment.put("contentBytes", encoded);
        attachments.put(attachment);
        message.put("attachments", attachments);

        JSONObject payload = new JSONObject();
        payload.put("message", message);
        payload.put("saveToSentItems", false);

        URL url = new URL("https://graph.microsoft.com/v1.0/users/" + FROM_EMAIL + "/sendMail");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.toString().getBytes());
        }

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        System.out.println(new BufferedReader(new InputStreamReader(
                conn.getInputStream())).lines().collect(Collectors.joining("\n")));
    }
}
