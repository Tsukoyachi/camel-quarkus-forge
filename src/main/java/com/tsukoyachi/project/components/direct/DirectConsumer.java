package com.tsukoyachi.project.components.direct;

import com.tsukoyachi.project.*;

public class DirectConsumer implements Consumer {
    private final ComponentConfiguration config;
    
    public DirectConsumer(ComponentConfiguration config) {
        this.config = config;
    }
    
    @Override
    public String getEndpoint() {
        String name = (String) config.getProperties().get("name");
        return "direct:" + name;
    }
}