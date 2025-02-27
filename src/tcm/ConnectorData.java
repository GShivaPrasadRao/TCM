import java.util.List;
import java.util.Map;

public class ConnectorData {
    private Map<String, List<ADOConnector>> connectors;

    public Map<String, List<ADOConnector>> getConnectors() {
        return connectors;
    }

    public void setConnectors(Map<String, List<ADOConnector>> connectors) {
        this.connectors = connectors;
    }
}

class ADOConnector {
    
    private String toolAPIURL;
    private String toolProject;
    private String ckProject;
    private String password;
    private String username;
    private List<String> documents;
    private String startDate;
    private String teamName;
    private String areaPath;

    // Getters & Setters
    public String getToolAPIURL() { return toolAPIURL; }
    public void setToolAPIURL(String toolAPIURL) { this.toolAPIURL = toolAPIURL; }

    public String getToolProject() { return toolProject; }
    public void setToolProject(String toolProject) { this.toolProject = toolProject; }

    public String getCkProject() { return ckProject; }
    public void setCkProject(String ckProject) { this.ckProject = ckProject; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<String> getDocuments() { return documents; }
    public void setDocuments(List<String> documents) { this.documents = documents; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getAreaPath() { return areaPath; }
    public void setAreaPath(String areaPath) { this.areaPath = areaPath; }
}
