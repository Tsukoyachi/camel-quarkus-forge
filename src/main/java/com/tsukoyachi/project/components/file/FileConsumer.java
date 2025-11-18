package com.tsukoyachi.project.components.file;

import java.util.Map;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.FileEndpointBuilderFactory.FileEndpointConsumerBuilder;
import org.apache.camel.model.RouteDefinition;

import com.tsukoyachi.project.*;
import com.tsukoyachi.project.interfaces.CamelConsumer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileConsumer implements CamelConsumer {
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
    public RouteDefinition configureFrom(EndpointRouteBuilder routeBuilder, String routeId) {
        if (config == null) {
            throw new IllegalStateException("Component not configured. Call configure() first.");
        }

        Map<String, Object> props = config.getProperties();
        String path = (String) props.remove("path");

        FileEndpointConsumerBuilder builder = routeBuilder.file(path);

        props.forEach(builder::doSetProperty);

        return routeBuilder.from(builder).routeId(routeId);
    }
}