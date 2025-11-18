package com.tsukoyachi.project.components.ibmmq;

import java.util.Map;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.RouteDefinition;

import com.tsukoyachi.project.*;
import com.tsukoyachi.project.interfaces.CamelConsumer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IbmmqConsumer implements CamelConsumer {
    private ComponentConfiguration config;
    
    public IbmmqConsumer() {
        // Default constructor for CDI
    }
    
    @Override
    public String getName() {
        return "ibmmq";
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
        String queueName = (String) props.remove("queue");
        
        if (queueName == null) {
            throw new IllegalArgumentException("Property 'queue' is required for IbmmqConsumer");
        }

        // Utilisation du composant JMS avec la connectionFactory IBM MQ
        var builder = routeBuilder.jms(queueName)
            .connectionFactory("#ibmMQConnectionFactory");

        // Configuration des propriétés restantes
        props.forEach(builder::doSetProperty);

        return routeBuilder.from(builder).routeId(routeId);
    }
}