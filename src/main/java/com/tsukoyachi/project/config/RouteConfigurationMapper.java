package com.tsukoyachi.project.config;

import com.tsukoyachi.project.ComponentConfiguration;
import com.tsukoyachi.project.RouteConfiguration;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class RouteConfigurationMapper {
    
    @Inject
    RoutesConfig routesConfig;
    
    public List<RouteConfiguration> mapToRouteConfigurations() {
        return routesConfig.definitions().stream()
            .map(this::mapToRouteConfiguration)
            .collect(Collectors.toList());
    }
    
    private RouteConfiguration mapToRouteConfiguration(RoutesConfig.RouteConfig routeConfig) {
        RouteConfiguration route = new RouteConfiguration();
        route.setId(routeConfig.id());
        route.setSource(mapToComponentConfiguration(routeConfig.source()));
        route.setDestination(mapToComponentConfiguration(routeConfig.destination()));
        return route;
    }
    
    private ComponentConfiguration mapToComponentConfiguration(RoutesConfig.ComponentConfig componentConfig) {
        ComponentConfiguration config = new ComponentConfiguration();
        config.setType(componentConfig.type());
        
        // Convert String properties to Object properties
        Map<String, Object> properties = new HashMap<>();
        componentConfig.properties().forEach((key, value) -> {
            // Try to convert numeric strings to appropriate types
            if (value.matches("\\d+")) {
                properties.put(key, Integer.parseInt(value));
            } else if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                properties.put(key, Boolean.parseBoolean(value));
            } else {
                properties.put(key, value);
            }
        });
        config.setProperties(properties);
        
        return config;
    }
}