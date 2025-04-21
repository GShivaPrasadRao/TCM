public class ServiceStatus {
    public class ServiceStatus {
    private String url;
    private String status;
    private String message;
    private String version;
    private String environment;
    private String responseTime;
    private String lastDeployed;

    // Constructor, Getters, Setters
}


    public ServiceStatus(String url) {
        this.url = url;
    }

    // Getters & Setters
    public String getUrl() { return url; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
