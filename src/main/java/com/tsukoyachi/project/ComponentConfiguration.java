package com.tsukoyachi.project;
import java.util.Map;

public class ComponentConfiguration {
    private String type; // "file", "direct", "http", etc.
    private Map<String, Object> properties;
    
    public ComponentConfiguration() {}
    
    public ComponentConfiguration(String type, Map<String, Object> properties) {
        this.type = type;
        this.properties = properties;
    }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}