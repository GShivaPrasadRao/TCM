import java.util.Map;

public class TestReportSender {
    public static void main(String[] args) {
        int totalTests = CucumberTestListener.getTotalTests();
        int passedTests = CucumberTestListener.getPassedTests();
        int failedTests = CucumberTestListener.getFailedTests();
        Map<String, Integer> tagWiseCount = CucumberTestListener.getTagWiseCount();
        Map<String, Integer> tagWisePass = CucumberTestListener.getTagWisePass();
        Map<String, Integer> tagWiseFail = CucumberTestListener.getTagWiseFail();

        double passPercentage = (totalTests == 0) ? 0 : (passedTests * 100.0) / totalTests;
        double failPercentage = (totalTests == 0) ? 0 : (failedTests * 100.0) / totalTests;

        StringBuilder report = new StringBuilder();
        report.append("### 🏆 Cucumber Test Execution Summary \\n")
              .append("| Metric | Count |\\n")
              .append("|--------|------|\\n")
              .append("| Total Test Cases | ").append(totalTests).append(" |\\n")
              .append("| Passed Test Cases | ").append(passedTests).append(" |\\n")
              .append("| Failed Test Cases | ").append(failedTests).append(" |\\n")
              .append("| ✅ Pass % Coverage | ").append(String.format("%.2f", passPercentage)).append("% |\\n")
              .append("| ❌ Fail % Coverage | ").append(String.format("%.2f", failPercentage)).append("% |\\n\\n");

        report.append("### 📌 Tag-wise Test Execution \\n")
              .append("| Tag | Total | Passed | Failed | Pass % |\\n")
              .append("|------|------|------|------|------|\\n");

        for (String tag : tagWiseCount.keySet()) {
            int total = tagWiseCount.getOrDefault(tag, 0);
            int passed = tagWisePass.getOrDefault(tag, 0);
            int failed = tagWiseFail.getOrDefault(tag, 0);
            double tagPassPercentage = (total == 0) ? 0 : (passed * 100.0) / total;

            report.append("| ").append(tag).append(" | ")
                  .append(total).append(" | ")
                  .append(passed).append(" | ")
                  .append(failed).append(" | ")
                  .append(String.format("%.2f", tagPassPercentage)).append("% |\\n");
        }

        sendToTeams(report.toString());
    }

    private static void sendToTeams(String jsonPayload) {
        // Implement HTTP request to send message to Teams Webhook
    }
}
    