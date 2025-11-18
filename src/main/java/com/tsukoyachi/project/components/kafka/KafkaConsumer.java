package com.tsukoyachi.project.components.kafka;

import java.util.Map;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.KafkaEndpointBuilderFactory.KafkaEndpointConsumerBuilder;
import org.apache.camel.model.RouteDefinition;

import com.tsukoyachi.project.*;
import com.tsukoyachi.project.interfaces.CamelConsumer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KafkaConsumer implements CamelConsumer {
    private ComponentConfiguration config;
    
    public KafkaConsumer() {
        // Default constructor for CDI
    }
    
    @Override
    public String getName() {
        return "kafka";
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
        String topic = (String) props.remove("topic");

        if (topic == null) {
            throw new IllegalArgumentException("Property 'topic' is required for a KafkaConsumer");
        }

        KafkaEndpointConsumerBuilder builder = routeBuilder.kafka(topic);

        props.forEach(builder::doSetProperty);

        return routeBuilder.from(builder).routeId(routeId);
    }
}