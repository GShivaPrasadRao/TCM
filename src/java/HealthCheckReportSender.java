import io.restassured.RestAssured;
import java.util.Map;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class HealthCheckReportSender {

    public static void sendReport(Map<String, Map<String, String>> appServiceStatus) {

        StringBuilder report = new StringBuilder();
        report.append("**Health Check Report Summary**\n\n");

        for (String appName : appServiceStatus.keySet()) {
            report.append("**Application:** ").append(appName).append("\n");
            Map<String, String> services = appServiceStatus.get(appName);
            for (String serviceName : services.keySet()) {
                report.append("- ").append(serviceName).append(": ").append(services.get(serviceName)).append("\n");
            }
            report.append("\n");
        }

        sendToTeams(report.toString());
        sendEmail(report.toString());
    }

    private static void sendToTeams() {
    String teamsWebhookUrl = "YOUR_TEAMS_WEBHOOK_URL";

    List<Map<String, Object>> sections = new ArrayList<>();

    for (String appName : HealthCheckHelper.appServiceStatus.keySet()) {
        Map<String, String> services = HealthCheckHelper.appServiceStatus.get(appName);

        boolean isOverallUp = true;
        List<Map<String, Object>> facts = new ArrayList<>();

        for (Map.Entry<String, String> serviceEntry : services.entrySet()) {
            String serviceName = serviceEntry.getKey();
            String status = serviceEntry.getValue();
            String emoji = status.equalsIgnoreCase("UP") ? "🟢" : "🔴";

            if (!status.equalsIgnoreCase("UP")) {
                isOverallUp = false;
            }

            Map<String, Object> fact = new HashMap<>();
            fact.put("name", serviceName);
            fact.put("value", emoji + " " + status);
            facts.add(fact);
        }

        String overallStatusEmoji = isOverallUp ? "🟢" : "🔴";
        String activityTitle = "🏢 " + appName + " (Overall: " + overallStatusEmoji + (isOverallUp ? " UP" : " DOWN") + ")";

        Map<String, Object> section = new HashMap<>();
        section.put("activityTitle", activityTitle);
        section.put("facts", facts);
        section.put("markdown", true);

        sections.add(section);
    }

    Map<String, Object> payload = new HashMap<>();
    payload.put("@type", "MessageCard");
    payload.put("@context", "http://schema.org/extensions");
    payload.put("themeColor", "0076D7");
    payload.put("summary", "Health Check Report");
    payload.put("title", "🛡️ Health Check Report");
    payload.put("sections", sections);

    RestAssured.given()
        .header("Content-Type", "application/json")
        .body(payload)
        .post(teamsWebhookUrl)
        .then()
        .statusCode(200);
}


    private static void sendEmailReport() {
    // SMTP Config
    String host = "smtp.yourserver.com"; // example: smtp.office365.com
    String port = "587";
    String username = "your-email@example.com";
    String password = "your-password";

    String fromEmail = "your-email@example.com";
    String toEmail = "recipient@example.com";

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", host);
    props.put("mail.smtp.port", port);

    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }l̥
    });

    try {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("🛡️ Health Check Report");

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<h1>🛡️ Health Check Report</h1>");

        for (String appName : HealthCheckHelper.appServiceStatus.keySet()) {
            Map<String, String> services = HealthCheckHelper.appServiceStatus.get(appName);

            boolean isOverallUp = true;
            StringBuilder table = new StringBuilder();
            table.append("<table border='1' cellpadding='5' cellspacing='0'>");
            table.append("<tr><th>Service</th><th>Status</th></tr>");

            for (Map.Entry<String, String> serviceEntry : services.entrySet()) {
                String serviceName = serviceEntry.getKey();
                String status = serviceEntry.getValue();
                String emoji = status.equalsIgnoreCase("UP") ? "🟢" : "🔴";

                if (!status.equalsIgnoreCase("UP")) {
                    isOverallUp = false;
                }

                table.append("<tr>");
                table.append("<td>").append(serviceName).append("</td>");
                table.append("<td>").append(emoji).append(" ").append(status).append("</td>");
                table.append("</tr>");
            }

            table.append("</table>");

            String overallStatusEmoji = isOverallUp ? "🟢" : "🔴";
            emailContent.append("<h2>🏢 " + appName + " (Overall: " + overallStatusEmoji + (isOverallUp ? " UP" : " DOWN") + ")</h2>");
            emailContent.append(table);
            emailContent.append("<br>");
        }

        message.setContent(emailContent.toString(), "text/html");

        Transport.send(message);

        System.out.println("✅ Email Report Sent Successfully!");

    } catch (MessagingException e) {
        e.printStackTrace();
    }
}

}
