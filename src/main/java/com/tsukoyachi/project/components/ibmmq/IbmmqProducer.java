package com.tsukoyachi.project.components.ibmmq;

import java.util.Map;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.ProcessorDefinition;

import com.tsukoyachi.project.*;
import com.tsukoyachi.project.interfaces.CamelProducer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IbmmqProducer implements CamelProducer {
    private ComponentConfiguration config;
    
    public IbmmqProducer() {
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
    public ProcessorDefinition<?> configureTo(EndpointRouteBuilder routeBuilder, ProcessorDefinition<?> processorDefinition) {
        if (config == null) {
            throw new IllegalStateException("Component not configured. Call configure() first.");
        }

        Map<String, Object> props = config.getProperties();
        String queueName = (String) props.remove("queue");
        
        if (queueName == null) {
            throw new IllegalArgumentException("Property 'queue' is required for IbmmqProducer");
        }

        // Utilisation du composant JMS avec la connectionFactory IBM MQ
        var builder = routeBuilder.jms(queueName)
            .connectionFactory("#ibmMQConnectionFactory");

        // Configuration des propriétés restantes
        props.forEach(builder::doSetProperty);

        return processorDefinition.to(builder);
    }
}