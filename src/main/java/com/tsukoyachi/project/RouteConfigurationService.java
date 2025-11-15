package com.tsukoyachi.project;
import jakarta.enterprise.context.ApplicationScoped;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;

@ApplicationScoped
public class RouteConfigurationService {
    
    public List<RouteConfiguration> loadRouteConfigurations() {
        // Load from routes.yml
        InputStream inputStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream("routes.yml");
        
        if (inputStream == null) {
            throw new RuntimeException("File routes.yml not found");
        }
        
        Yaml yaml = new Yaml();
        RoutesWrapper wrapper = yaml.loadAs(inputStream, RoutesWrapper.class);
        return wrapper.getRoutes();
    }
    
    // Wrapper class for YAML
    public static class RoutesWrapper {
        private List<RouteConfiguration> routes;
        
        public List<RouteConfiguration> getRoutes() { return routes; }
        public void setRoutes(List<RouteConfiguration> routes) { this.routes = routes; }
    }
}