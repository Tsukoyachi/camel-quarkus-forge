package com.tsukoyachi.project;
import jakarta.enterprise.context.ApplicationScoped;

import com.tsukoyachi.project.components.file.FileConsumer;
import com.tsukoyachi.project.components.file.FileProducer;
import com.tsukoyachi.project.components.direct.DirectConsumer;
import com.tsukoyachi.project.components.direct.DirectProducer;
import com.tsukoyachi.project.components.timer.TimerConsumer;
import com.tsukoyachi.project.components.log.LogProducer;

@ApplicationScoped
public class ComponentFactory {
    
    public Consumer createConsumer(ComponentConfiguration config) {
        return switch (config.getType().toLowerCase()) {
            case "file" -> new FileConsumer(config);
            case "direct" -> new DirectConsumer(config);
            case "timer" -> new TimerConsumer(config);
            // Add other types as needed
            default -> throw new IllegalArgumentException("Unsupported consumer type: " + config.getType());
        };
    }
    
    public Producer createProducer(ComponentConfiguration config) {
        return switch (config.getType().toLowerCase()) {
            case "file" -> new FileProducer(config);
            case "direct" -> new DirectProducer(config);
            case "log" -> new LogProducer(config);
            // Add other types as needed
            default -> throw new IllegalArgumentException("Unsupported producer type: " + config.getType());
        };
    }
    
    public org.apache.camel.Processor createProcessor(ProcessorConfiguration config) {
        return switch (config.getType().toLowerCase()) {
            case "transform" -> new TransformProcessor(config);
            case "filter" -> new FilterProcessor(config);
            case "custom" -> new CustomProcessor(config);
            default -> throw new IllegalArgumentException("Unsupported processor type: " + config.getType());
        };
    }
}