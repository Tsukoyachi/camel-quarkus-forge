package com.tsukoyachi.project.components.direct;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.DirectEndpointBuilderFactory.DirectEndpointConsumerBuilder;
import org.apache.camel.model.RouteDefinition;

import com.tsukoyachi.project.*;
import com.tsukoyachi.project.interfaces.CamelConsumer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DirectConsumer implements CamelConsumer {
    private ComponentConfiguration config;
    
    public DirectConsumer() {
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
    public RouteDefinition configureFrom(EndpointRouteBuilder routeBuilder, String routeId) {
        if (config == null) {
            throw new IllegalStateException("Component not configured. Call configure() first.");
        }

        String name = (String) config.getProperties().get("name");
        if (name == null) {
            throw new IllegalArgumentException("Property 'name' is required for DirectConsumer");
        }

        DirectEndpointConsumerBuilder builder = routeBuilder.direct(name);

        return routeBuilder.from(builder).routeId(routeId);
    }
}