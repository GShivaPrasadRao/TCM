import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {
    private static final String SMTP_HOST = "smtp.gmail.com"; // Your SMTP server
    private static final String SMTP_PORT = "587";
    private static final String USERNAME = "your-email@gmail.com"; // Sender email
    private static final String PASSWORD = "your-email-password"; // App Password

    public static void sendEmail(List<Map<String, String>> testResults) {
        String recipient = "recipient-email@example.com"; // Change to the actual recipient email
        String subject = "📌 Test Suite Execution Report";

        // Construct HTML Report
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("<h2>✅ Test Execution Summary</h2>");
        emailBody.append("<table border='1' cellspacing='0' cellpadding='5'>");
        emailBody.append("<tr><th>Connector</th><th>Status</th><th>Duration</th></tr>");

        for (Map<String, String> result : testResults) {
            emailBody.append("<tr>")
                     .append("<td>").append(result.get("Connector")).append("</td>")
                     .append("<td>").append(result.get("Status")).append("</td>")
                     .append("<td>").append(result.get("Duration")).append("</td>")
                     .append("</tr>");
        }
        emailBody.append("</table>");

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setContent(emailBody.toString(), "text/html");

            Transport.send(message);
            System.out.println("📩 Email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
