import java.util.*;

public class TestResultStorage {
    private static final List<Map<String, String>> testResults = new ArrayList<>();

    // Store test result for each connector
    public static void addTestResult(String connectorName, String status, String duration) {
        Map<String, String> result = new HashMap<>();
        result.put("Connector", connectorName);
        result.put("Status", status);
        result.put("Duration", duration);
        testResults.add(result);
    }

    // Get all test results
    public static List<Map<String, String>> getTestResults() {
        return testResults;
    }
}
g