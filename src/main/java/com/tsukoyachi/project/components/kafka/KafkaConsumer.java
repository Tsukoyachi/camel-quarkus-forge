package com.tsukoyachi.project.components.kafka;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.tsukoyachi.project.*;
import com.tsukoyachi.project.interfaces.CamelConsumer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KafkaConsumer implements CamelConsumer {
    private ComponentConfiguration config;
    
    public KafkaConsumer() {
        // Default constructor for CDI
    }
    
    @Override
    public String getName() {
        return "kafka";
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
        
        Map<String, Object> props = config.getProperties();
        String topic = (String) props.remove("topic");
        
        if (topic == null) {
            throw new IllegalArgumentException("Property 'topic' is required for a KafkaConsumer");
        }
        
        StringBuilder endpoint = new StringBuilder("kafka:");
        endpoint.append(topic);
        
        // Build all parameters dynamically except 'path'
        String queryParams = props.entrySet().stream()
            .filter(entry -> entry.getValue() != null)       // Exclude null values
            .map(this::formatValue)
            .collect(Collectors.joining("&"));
        
        if (!queryParams.isEmpty()) {
            endpoint.append("?").append(queryParams);
        }
        
        System.out.println(endpoint.toString());
        return endpoint.toString();
    }

    public String formatValue(Entry<String, Object> entry) {
        String key = entry.getKey().trim();
        String value = entry.getValue().toString().trim();
        try {
            String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);
            return key + "=" + encodedValue;
        } catch (Exception e) {
            return key + "=" + value; // Fallback if encoding fails
        }
    }
}