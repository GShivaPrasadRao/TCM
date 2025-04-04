// Project Creation Job (Run 1)
public class ProjectCreationService {

    private String executionId = "RUN_20250404_0804";

    public String getOrCreateSharedProjectId() {
        String projectId = FileUtils.getActiveProjectIdForExecution(executionId);

        if (projectId == null) {
            projectId = createNewProject("PROJECT_04042025_0804");
            if (FileUtils.insertProjectForExecution(executionId, projectId)) {
                System.out.println("Project created and stored in file (Run 1) with ID: " + projectId + " for execution: " + executionId);
            } else {
                System.err.println("Error storing Project ID in file for execution: " + executionId);
            }
        } else {
            System.out.println("Using existing Project ID from file (Run 1): " + projectId + " for execution: " + executionId);
        }
        return projectId;
    }

    private String createNewProject(String projectName) {
        return projectName;
    }
}

// Connector Creation Job (Run 1)
public class ConnectorCreationService {

    private String executionId = "RUN_20250404_0804";

    public void createConnectors() {
        String projectId = FileUtils.getActiveProjectIdForExecution(executionId);
        if (projectId != null) {
            System.out.println("Creating connectors for Project ID: " + projectId + " (Run 1, retrieved from file)");
        } else {
            System.err.println("Active Project ID not found in file for execution " + executionId + ".");
        }
    }
}
// etc for other jobs