package com.tsukoyachi.project.components.log;

import java.util.Map;

import com.tsukoyachi.project.*;
import com.tsukoyachi.project.interfaces.CamelProducer;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.LogEndpointBuilderFactory.LogEndpointBuilder;
import org.apache.camel.model.ProcessorDefinition;

@ApplicationScoped
public class LogProducer implements CamelProducer {
    private ComponentConfiguration config;
    
    public LogProducer() {
        // Default constructor for CDI
    }
    
    @Override
    public String getName() {
        return "log";
    }
    
    @Override
    public void configure(ComponentConfiguration config) {
        this.config = config;
    }
    
    @Override
    public ProcessorDefinition<?> configureTo(EndpointRouteBuilder routeBuilder, ProcessorDefinition<?> processorDefinition) {
        if (config == null) {
            throw new IllegalStateException("Component not configured. Call configure() first.");
        }

        Map<String, Object> props = config.getProperties();
        String name = (String) props.remove("name");
        
        if (name == null) {
            throw new IllegalArgumentException("Property 'name' is required for LogProducer");
        }

        LogEndpointBuilder builder = routeBuilder.log(name);

        props.forEach(builder::doSetProperty);

        return processorDefinition.to(builder);
    }
}