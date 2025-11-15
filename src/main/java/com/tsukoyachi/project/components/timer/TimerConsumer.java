package com.tsukoyachi.project.components.timer;

import com.tsukoyachi.project.*;

public class TimerConsumer implements Consumer {
    private final ComponentConfiguration config;
    
    public TimerConsumer(ComponentConfiguration config) {
        this.config = config;
    }
    
    @Override
    public String getEndpoint() {
        String name = (String) config.getProperties().get("name");
        Integer period = (Integer) config.getProperties().getOrDefault("period", 5000);
        return "timer:" + name + "?period=" + period;
    }
}