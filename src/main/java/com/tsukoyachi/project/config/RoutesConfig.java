package com.tsukoyachi.project.config;

import io.smallrye.config.ConfigMapping;
import java.util.List;
import java.util.Map;

@ConfigMapping(prefix = "camel.routes")
public interface RoutesConfig {
    
    List<RouteConfig> definitions();
    
    interface RouteConfig {
        String id();
        ComponentConfig source();
        ComponentConfig destination();
    }
    
    interface ComponentConfig {
        String type();
        Map<String, String> properties();
    }
}