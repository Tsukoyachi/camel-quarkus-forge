package com.tsukoyachi.project;
import java.util.Map;

public class ProcessorConfiguration {
    private String type; // "transform", "filter", "custom", etc.
    private Map<String, Object> properties;
    
    public ProcessorConfiguration() {}
    
    public ProcessorConfiguration(String type, Map<String, Object> properties) {
        this.type = type;
        this.properties = properties;
    }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Map<String, Object> getProperties() { return properties; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
}