package com.tsukoyachi.project;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ComponentFactory {
    
    private final Map<String, Consumer> consumerMap = new HashMap<>();
    private final Map<String, Producer> producerMap = new HashMap<>();
    
    @Inject
    public ComponentFactory(Instance<Consumer> consumers, Instance<Producer> producers) {
        // Build the consumer map
        for (Consumer consumer : consumers) {
            consumerMap.put(consumer.getName(), consumer);
        }
        
        // Build the producer map
        for (Producer producer : producers) {
            producerMap.put(producer.getName(), producer);
        }
        
        System.out.println("Registered consumers: " + consumerMap.keySet());
        System.out.println("Registered producers: " + producerMap.keySet());
    }
    
    public Consumer createConsumer(ComponentConfiguration config) {
        Consumer consumer = consumerMap.get(config.getType().toLowerCase());
        if (consumer == null) {
            throw new IllegalArgumentException("Unsupported consumer type: " + config.getType() + 
                ". Available types: " + consumerMap.keySet());
        }
        
        // Configure the consumer with the provided configuration
        consumer.configure(config);
        return consumer;
    }
    
    public Producer createProducer(ComponentConfiguration config) {
        Producer producer = producerMap.get(config.getType().toLowerCase());
        if (producer == null) {
            throw new IllegalArgumentException("Unsupported producer type: " + config.getType() + 
                ". Available types: " + producerMap.keySet());
        }
        
        // Configure the producer with the provided configuration
        producer.configure(config);
        return producer;
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