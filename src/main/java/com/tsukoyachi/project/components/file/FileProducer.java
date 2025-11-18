package com.tsukoyachi.project.components.file;

import java.util.Map;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.FileEndpointBuilderFactory.FileEndpointProducerBuilder;
import org.apache.camel.model.ProcessorDefinition;

import com.tsukoyachi.project.*;
import com.tsukoyachi.project.interfaces.CamelProducer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileProducer implements CamelProducer {
    private ComponentConfiguration config;
    
    public FileProducer() {
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
    public ProcessorDefinition<?> configureTo(EndpointRouteBuilder routeBuilder, ProcessorDefinition<?> processorDefinition) {
        if (config == null) {
            throw new IllegalStateException("Component not configured. Call configure() first.");
        }

        Map<String, Object> props = config.getProperties();
        String path = (String) props.remove("path");

        FileEndpointProducerBuilder builder = routeBuilder.file(path);

        props.forEach(builder::doSetProperty);

        return processorDefinition.to(builder);
    }
}