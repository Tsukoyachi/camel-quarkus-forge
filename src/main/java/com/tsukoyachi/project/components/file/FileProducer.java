package com.tsukoyachi.project.components.file;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.tsukoyachi.project.*;

public class FileProducer implements Producer {
    private final ComponentConfiguration config;
    
    public FileProducer(ComponentConfiguration config) {
        this.config = config;
    }
    
    @Override
    public String getEndpoint() {
        Map<String, Object> props = config.getProperties();
        String path = (String) props.get("path");
        
        if (path == null) {
            throw new IllegalArgumentException("Property 'path' is required for a FileProducer");
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