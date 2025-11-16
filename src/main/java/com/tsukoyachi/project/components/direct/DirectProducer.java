package com.tsukoyachi.project.components.direct;

import com.tsukoyachi.project.*;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DirectProducer implements Producer {
    private ComponentConfiguration config;
    
    public DirectProducer() {
        // Default constructor for CDI
    }
    
    @Override
    public String getName() {
        return "direct";
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
        
        String name = (String) config.getProperties().get("name");
        return "direct:" + name;
    }
}