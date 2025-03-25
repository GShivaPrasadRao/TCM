import io.cucumber.java.AfterAll;
import java.util.List;
import java.util.Map;

public class Hooks {

    private static ExtentReports extent;
    private static ExtentTest test;
    private static JsonDataReader jsonReader;

    @Before
    public void setUp(Scenario scenario) {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
        test = extent.createTest(scenario.getName());

        // Load JSON file based on feature name (assumes JSON file follows feature naming convention)
        String featureName = scenario.getUri().toString().replaceAll(".*/", "").replace(".feature", "") + ".json";
        jsonReader = new JsonDataReader(featureName);
    }

    @After
    public void tearDown(Scenario scenario) {
        test.info("Test Data Used: " + jsonReader.getJsonDataAsString());

        if (scenario.isFailed()) {
            test.fail("Test Failed!");
        } else {
            test.pass("Test Passed!");
        }
        extent.flush();
    }

    public static ExtentTest getTest() {
        return test;
    }

    public static JsonDataReader getJsonReader() {
        return jsonReader;
    }

    @AfterAll
    public static void sendFinalTestReport() {
        System.out.println("✅ All test cases executed. Sending final report...");
        List<Map<String, String>> results = TestResultStorage.getTestResults();
        EmailSender.sendEmail(results);
    }
}
