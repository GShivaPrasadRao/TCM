public class TeamsNotifier {

    static String webhookUrl = "https://outlook.office.com/webhook/your-webhook-url";

    public static void sendReport(Map<String, List<ServiceStatus>> projectMap) {
        StringBuilder msg = new StringBuilder();
        msg.append("**🚀 Microservices Health Check Report**\n");

        for (Map.Entry<String, List<ServiceStatus>> entry : projectMap.entrySet()) {
            msg.append("\n**Project: ").append(entry.getKey()).append("**\n");

            for (ServiceStatus s : entry.getValue()) {
                msg.append("- ").append(s.getUrl())
                   .append(" : ").append(s.getStatus());

                if ("DOWN".equalsIgnoreCase(s.getStatus())) {
                    msg.append(" ❌");
                    if (s.getError() != null) msg.append(" - ").append(s.getError());
                } else if ("UP".equalsIgnoreCase(s.getStatus())) {
                    msg.append(" ✅");
                }

                msg.append("\n");
            }
        }

        sendToTeams(msg.toString());
    }

    private static void sendToTeams(String message) {
        Map<String, String> body = Map.of("text", message);

        RestAssured
            .given()
            .header("Content-Type", "application/json")
            .body(body)
            .post(webhookUrl);
    }
}
