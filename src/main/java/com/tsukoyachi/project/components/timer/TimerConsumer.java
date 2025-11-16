package com.tsukoyachi.project.components.timer;

import com.tsukoyachi.project.*;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TimerConsumer implements Consumer {
    private ComponentConfiguration config;
    
    public TimerConsumer() {
        // Default constructor for CDI
    }
    
    @Override
    public String getName() {
        return "timer";
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
        Integer period = (Integer) config.getProperties().getOrDefault("period", 5000);
        return "timer:" + name + "?period=" + period;
    }
}