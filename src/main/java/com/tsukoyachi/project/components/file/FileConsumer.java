package com.tsukoyachi.project.components.file;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.tsukoyachi.project.*;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileConsumer implements Consumer {
    private ComponentConfiguration config;
    
    public FileConsumer() {
        // Default constructor for CDI
    }
    
    @Override
    public String getName() {
        return "file";
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
        String path = (String) props.get("path");
        
        if (path == null) {
            throw new IllegalArgumentException("Property 'path' is required for a FileConsumer");
        }
        
        StringBuilder endpoint = new StringBuilder("file:");
        endpoint.append(path);
        
        // Build all parameters dynamically except 'path'
        String queryParams = props.entrySet().stream()
            .filter(entry -> !"path".equals(entry.getKey())) // Exclude 'path'
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