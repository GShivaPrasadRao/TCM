import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    public static void handleException(Exception e) {
        if (e instanceof NoSuchElementException) {
            logger.error("❌ Element Not Found: {}", e.getMessage());
        } else if (e instanceof TimeoutException) {
            logger.error("⏳ Timeout Occurred: {}", e.getMessage());
        } else if (e instanceof WebDriverException) {
            logger.error("🚗 WebDriver Exception: {}", e.getMessage());
        } else {
            logger.error("⚠️ Unhandled Exception: {}", e.getMessage());
        }

        // Fail the test explicitly
        throw new RuntimeException("Test execution stopped due to an exception: " + e.getMessage());
    }
}
