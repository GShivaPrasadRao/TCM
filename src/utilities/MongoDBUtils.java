import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Date;

public class MongoDBUtils {

    private static final String CONNECTION_STRING = "mongodb://your_mongodb_connection_string"; // Replace
    private static final String DATABASE_NAME = "your_database_name"; // Replace
    private static final String COLLECTION_NAME = "test_execution_projects";

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    static {
        mongoClient = MongoClients.create(CONNECTION_STRING);
        database = mongoClient.getDatabase(DATABASE_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    public static String getActiveProjectIdForExecution(String executionId) {
        Document project = collection.find(Filters.and(Filters.eq("executionId", executionId), Filters.eq("isActive", true))).first();
        if (project != null) {
            return project.getString("projectId");
        }
        return null;
    }

    public static boolean insertProjectForExecution(String executionId, String projectId) {
        Document project = new Document()
                .append("executionId", executionId)
                .append("projectId", projectId)
                .append("isActive", true)
                .append("createdAt", Date.from(Instant.now()))
                .append("updatedAt", Date.from(Instant.now()));
        try {
            collection.insertOne(project);
            return true;
        } catch (Exception e) {
            System.err.println("Error inserting project for execution " + executionId + ": " + e.getMessage());
            return false;
        }
    }

    public static boolean deactivateAllProjectsForExecution(String executionId) {
        try {
            collection.updateMany(Filters.eq("executionId", executionId), Updates.set("isActive", false));
            return true;
        } catch (Exception e) {
            System.err.println("Error deactivating projects for execution " + executionId + ": " + e.getMessage());
            return false;
        }
    }

    public static boolean activateProjectForExecution(String executionId, String projectId) {
        boolean success = deactivateAllProjectsForExecution(executionId);
        if (success) {
            try {
                collection.updateOne(Filters.and(Filters.eq("executionId", executionId), Filters.eq("projectId", projectId)), Updates.set("isActive", true));
                return true;
            } catch (Exception e) {
                System.err.println("Error activating project for execution " + executionId + ": " + e.getMessage());
                return false;
            }
        }
        return false;
    }
}