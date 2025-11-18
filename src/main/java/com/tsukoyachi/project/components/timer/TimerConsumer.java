package com.tsukoyachi.project.components.timer;

import java.util.Map;

import com.tsukoyachi.project.*;
import com.tsukoyachi.project.interfaces.CamelConsumer;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.TimerEndpointBuilderFactory.TimerEndpointBuilder;
import org.apache.camel.model.RouteDefinition;

@ApplicationScoped
public class TimerConsumer implements CamelConsumer {
    private ComponentConfiguration config;
    
    public TimerConsumer() {
        // Default constructor for CDI
    }
    
    @Override
    public String getName() {
        return "timer";
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
        String name = (String) props.remove("name");
        
        if (name == null) {
            throw new IllegalArgumentException("Property 'name' is required for TimerConsumer");
        }
        
        TimerEndpointBuilder builder = routeBuilder.timer(name);
        
        props.forEach(builder::doSetProperty);
        
        return routeBuilder.from(builder).routeId(routeId);
    }
}