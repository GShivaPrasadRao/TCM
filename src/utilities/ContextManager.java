import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ContextManager {
    private static String projectName;

    public static String generateProjectName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMMyyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

        String formattedDate = now.format(dateFormatter).toUpperCase(); // Example: 22MAR2025
        String formattedTime = now.format(timeFormatter); // Example: 1307

        projectName = "CKRT" + formattedDate + "_" + formattedTime;
        return projectName;
    }

    public static String getProjectName() {
        return projectName;
    }
}
