import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.Base64;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AzureUploader {
    // Replace with your actual values
    private static final String PERSONAL_ACCESS_TOKEN = "your-token";
    private static final String ORGANIZATION = "your-org";
    private static final String PROJECT = "your-project";
    private static final int TEST_PLAN_ID = 1;
    private static final int TEST_SUITE_ID = 1;
    private static final String BASE_URL = "https://dev.azure.com/" + ORGANIZATION;

    public static void main(String[] args) throws Exception {
        String featureFilePath = "src/test/resources/ado_connector.feature";
        List<Map<String, String>> testCases = parseFeatureFile(featureFilePath);

        for (Map<String, String> testCase : testCases) {
            createTestCaseInADO(testCase.get("title"), testCase.get("description"), testCase.get("expectedResult"));
        }
    }

    private static List<Map<String, String>> parseFeatureFile(String filePath) throws IOException {
        List<Map<String, String>> scenarios = new ArrayList<>();

        boolean insideMultilineComment = false;
        String currentScenarioTitle = null;
        List<String> steps = new ArrayList<>();
        String expectedResult = "";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                // Ignore single and multiline comments
                if (line.startsWith("/*")) {
                    insideMultilineComment = true;
                    continue;
                }
                if (line.endsWith("*/")) {
                    insideMultilineComment = false;
                    continue;
                }
                if (insideMultilineComment || line.startsWith("//") || line.isEmpty()) {
                    continue;
                }

                // Start of new scenario
                if (line.startsWith("Scenario:")) {
                    // Save previous scenario
                    if (currentScenarioTitle != null) {
                        Map<String, String> scenarioMap = new HashMap<>();
                        scenarioMap.put("title", currentScenarioTitle);
                        scenarioMap.put("description", String.join("<br>", steps));
                        scenarioMap.put("expectedResult", expectedResult);
                        scenarios.add(scenarioMap);

                        steps = new ArrayList<>();
                        expectedResult = "";
                    }

                    currentScenarioTitle = line.replace("Scenario:", "").trim();
                } else if (line.startsWith("Given") || line.startsWith("When") || line.startsWith("And")) {
                    steps.add(line);
                } else if (line.startsWith("Then")) {
                    expectedResult = line;
                }
            }

            // Save last scenario
            if (currentScenarioTitle != null) {
                Map<String, String> scenarioMap = new HashMap<>();
                scenarioMap.put("title", currentScenarioTitle);
                scenarioMap.put("description", String.join("<br>", steps));
                scenarioMap.put("expectedResult", expectedResult);
                scenarios.add(scenarioMap);
            }
        }

        return scenarios;
    }

    private static void createTestCaseInADO(String title, String steps, String expectedResult) throws Exception {
        OkHttpClient client = new OkHttpClient();

        String credentials = ":" + PERSONAL_ACCESS_TOKEN;
        String basicAuth = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());

        String jsonBody = "[\n" +
                "  {\n" +
                "    \"op\": \"add\",\n" +
                "    \"path\": \"/fields/System.Title\",\n" +
                "    \"value\": \"" + title + "\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"op\": \"add\",\n" +
                "    \"path\": \"/fields/Microsoft.VSTS.TCM.Steps\",\n" +
                "    \"value\": \"" + steps.replace("\"", "\\\"") + "\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"op\": \"add\",\n" +
                "    \"path\": \"/fields/Microsoft.VSTS.TCM.ExpectedResult\",\n" +
                "    \"value\": \"" + expectedResult.replace("\"", "\\\"") + "\"\n" +
                "  }\n" +
                "]";

        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json-patch+json"));

        String url = BASE_URL + "/" + PROJECT + "/_apis/wit/workitems/$Test%20Case?api-version=6.0";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", basicAuth)
                .addHeader("Content-Type", "application/json-patch+json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                int testCaseId = mapper.readTree(responseBody).get("id").asInt();
                System.out.println("✅ Test Case Created with ID: " + testCaseId);

                addTestCaseToSuite(testCaseId, basicAuth);
            } else {
                System.err.println("❌ Failed to create test case: " + response.code() + " - " + response.message());
            }
        }
    }

    private static void addTestCaseToSuite(int testCaseId, String basicAuth) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String url = BASE_URL + "/" + PROJECT +
                "/_apis/test/Plans/" + TEST_PLAN_ID +
                "/suites/" + TEST_SUITE_ID +
                "/testcases/" + testCaseId +
                "?api-version=6.0";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", basicAuth)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create("", null))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("🧩 Test Case ID " + testCaseId + " added to Test Suite.");
            } else {
                System.err.println("❌ Failed to add test case to suite: " + response.code());
            }
        }
    }
}
