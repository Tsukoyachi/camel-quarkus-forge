package com.tsukoyachi.project.components.log;

import com.tsukoyachi.project.*;

public class LogProducer implements Producer {
    private final ComponentConfiguration config;
    
    public LogProducer(ComponentConfiguration config) {
        this.config = config;
    }
    
    @Override
    public String getEndpoint() {
        String name = (String) config.getProperties().getOrDefault("name", "log");
        return "log:" + name;
    }
}