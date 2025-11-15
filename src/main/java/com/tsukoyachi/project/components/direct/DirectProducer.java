package com.tsukoyachi.project.components.direct;

import com.tsukoyachi.project.*;

public class DirectProducer implements Producer {
    private final ComponentConfiguration config;
    
    public DirectProducer(ComponentConfiguration config) {
        this.config = config;
    }
    
    @Override
    public String getEndpoint() {
        String name = (String) config.getProperties().get("name");
        return "direct:" + name;
    }
}