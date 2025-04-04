import java.io.*;
import java.util.Properties;

public class FileUtils {

    private static final String FILE_PATH = "/path/to/shared_project.properties"; // Replace

    public static String getActiveProjectIdForExecution(String executionId) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(FILE_PATH)) {
            properties.load(fis);
            return properties.getProperty(executionId);
        } catch (IOException e) {
            System.err.println("Error reading project ID from file: " + e.getMessage());
            return null;
        }
    }

    public static boolean insertProjectForExecution(String executionId, String projectId) {
        Properties properties = new Properties();
        try {
            File file = new File(FILE_PATH);
            if(file.exists()){
                FileInputStream fis = new FileInputStream(FILE_PATH);
                properties.load(fis);
                fis.close();
            }

            properties.setProperty(executionId, projectId);
            try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
                properties.store(fos, "Shared Project IDs");
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error writing project ID to file: " + e.getMessage());
            return false;
        }
    }

    public static boolean removeProjectForExecution(String executionId) {
        Properties properties = new Properties();
        try {
            File file = new File(FILE_PATH);
            if(file.exists()){
                FileInputStream fis = new FileInputStream(FILE_PATH);
                properties.load(fis);
                fis.close();
            }
            properties.remove(executionId);
            try (FileOutputStream fos = new FileOutputStream(FILE_PATH)) {
                properties.store(fos, "Shared Project IDs");
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error writing project ID to file: " + e.getMessage());
            return false;
        }
    }
}