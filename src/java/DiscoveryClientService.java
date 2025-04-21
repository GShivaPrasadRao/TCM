public class DiscoveryClientUtil {

    static Map<String, String> discoveryClients = Map.of(
        "ProjectA", "http://projectA-discovery/api/services",
        "ProjectB", "http://projectB-discovery/api/services",
        "ProjectC", "http://projectC-discovery/api/services"
    );

    public static Map<String, List<ServiceStatus>> getServicesByProject(String token) {
        Map<String, List<ServiceStatus>> projectMap = new HashMap<>();

        for (Map.Entry<String, String> entry : discoveryClients.entrySet()) {
            String project = entry.getKey();
            String url = entry.getValue();

            try {
                Response response = RestAssured.given()
                    .header("Authorization", "Bearer " + token)
                    .get(url);

                List<String> serviceUrls = response.jsonPath().getList("services");
                List<ServiceStatus> services = serviceUrls.stream()
                    .map(ServiceStatus::new)
                    .collect(Collectors.toList());

                projectMap.put(project, services);
            } catch (Exception e) {
                System.out.println("Failed to fetch from " + url);
            }
        }

        return projectMap;
    }
}
