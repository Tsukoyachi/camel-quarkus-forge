package com.tsukoyachi.project;
import java.util.List;

public class RouteConfiguration {
    private String id;
    private ComponentConfiguration source;
    private ComponentConfiguration destination;
    private List<ProcessorConfiguration> processors;
    
    // Constructors
    public RouteConfiguration() {}
    
    public RouteConfiguration(String id, ComponentConfiguration source, ComponentConfiguration destination) {
        this.id = id;
        this.source = source;
        this.destination = destination;
    }
    
    // Getters/Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public ComponentConfiguration getSource() { return source; }
    public void setSource(ComponentConfiguration source) { this.source = source; }
    
    public ComponentConfiguration getDestination() { return destination; }
    public void setDestination(ComponentConfiguration destination) { this.destination = destination; }
    
    public List<ProcessorConfiguration> getProcessors() { return processors; }
    public void setProcessors(List<ProcessorConfiguration> processors) { this.processors = processors; }
}