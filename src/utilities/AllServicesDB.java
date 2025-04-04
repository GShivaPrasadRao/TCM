public class ProjectCreationService {

    private String executionId = "RUN_20250404_0804";

    public String getOrCreateSharedProjectId() {
        String projectId = MongoDBUtils.getActiveProjectIdForExecution(executionId);

        if (projectId == null) {
            projectId = createNewProject("PROJECT_04042025_0804");
            if (MongoDBUtils.insertProjectForExecution(executionId, projectId)) {
                System.out.println("Project created and stored in MongoDB (Run 1) with ID: " + projectId + " for execution: " + executionId);
            } else {
                System.err.println("Error storing Project ID in MongoDB for execution: " + executionId);
            }
        } else {
            System.out.println("Using existing Project ID from MongoDB (Run 1): " + projectId + " for execution: " + executionId);
        }
        return projectId;
    }

    private String createNewProject(String projectName) {
        return projectName;
    }
}


public class ConnectorCreationService {

    private String executionId = "RUN_20250404_0804";

    public void createConnectors() {
        String projectId = MongoDBUtils.getActiveProjectIdForExecution(executionId);
        if (projectId != null) {
            System.out.println("Creating connectors for Project ID: " + projectId + " (Run 1, retrieved from MongoDB)");
        } else {
            System.err.println("Active Project ID not found in MongoDB for execution " + executionId + ".");
        }
    }
}


public class NifiSyncService {

    private String executionId = "RUN_20250404_0804";

    public void performNifiSync() {
        String projectId = MongoDBUtils.getActiveProjectIdForExecution(executionId);
        if (projectId != null) {
            System.out.println("Performing Nifi Sync for Project ID: " + projectId + " (Run 1, retrieved from MongoDB)");
        } else {
            System.err.println("Active Project ID not found in MongoDB for execution " + executionId + ".");
        }
    }
}





