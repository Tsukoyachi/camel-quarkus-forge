package com.tsukoyachi.project.components.direct;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.DirectEndpointBuilderFactory.DirectEndpointProducerBuilder;
import org.apache.camel.model.ProcessorDefinition;

import com.tsukoyachi.project.*;
import com.tsukoyachi.project.interfaces.CamelProducer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DirectProducer implements CamelProducer {
    private ComponentConfiguration config;
    
    public DirectProducer() {
        // Default constructor for CDI
    }
    
    @Override
    public String getName() {
        return "direct";
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

        String name = (String) config.getProperties().get("name");
        if (name == null) {
            throw new IllegalArgumentException("Property 'name' is required for DirectProducer");
        }

        DirectEndpointProducerBuilder builder = routeBuilder.direct(name);

        return processorDefinition.to(builder);
    }
}