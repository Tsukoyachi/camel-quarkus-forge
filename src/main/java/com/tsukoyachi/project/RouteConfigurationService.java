package com.tsukoyachi.project;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.tsukoyachi.project.config.RouteConfigurationMapper;

import java.util.List;

@ApplicationScoped
public class RouteConfigurationService {
    
    @Inject
    RouteConfigurationMapper routeConfigurationMapper;
    
    public List<RouteConfiguration> loadRouteConfigurations() {
        // Load routes from application.yml configuration
        return routeConfigurationMapper.mapToRouteConfigurations();
    }
}