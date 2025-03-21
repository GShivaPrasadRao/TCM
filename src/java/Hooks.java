import io.cucumber.java.AfterAll;
import java.util.List;
import java.util.Map;

public class Hooks {

    @AfterAll
    public static void sendFinalTestReport() {
        System.out.println("✅ All test cases executed. Sending final report...");
        List<Map<String, String>> results = TestResultStorage.getTestResults();
        EmailSender.sendEmail(results);
    }
}
    