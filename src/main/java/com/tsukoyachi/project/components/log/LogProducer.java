package com.tsukoyachi.project.components.log;

import com.tsukoyachi.project.*;
import com.tsukoyachi.project.interfaces.CamelProducer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogProducer implements CamelProducer {
    private ComponentConfiguration config;
    
    public LogProducer() {
        // Default constructor for CDI
    }
    
    @Override
    public String getName() {
        return "log";
    }
    
    @Override
    public void configure(ComponentConfiguration config) {
        this.config = config;
    }
    
    @Override
    public String getEndpoint() {
        if (config == null) {
            throw new IllegalStateException("Component not configured. Call configure() first.");
        }
        
        String name = (String) config.getProperties().getOrDefault("name", "log");
        return "log:" + name;
    }
}