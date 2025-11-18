package com.tsukoyachi.project.components.kafka;

import java.util.Map;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.builder.endpoint.dsl.KafkaEndpointBuilderFactory.KafkaEndpointProducerBuilder;
import org.apache.camel.model.ProcessorDefinition;

import com.tsukoyachi.project.*;
import com.tsukoyachi.project.interfaces.CamelProducer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KafkaProducer implements CamelProducer {
    private ComponentConfiguration config;
    
    public KafkaProducer() {
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
    public ProcessorDefinition<?> configureTo(EndpointRouteBuilder routeBuilder, ProcessorDefinition<?> processorDefinition) {
        if (config == null) {
            throw new IllegalStateException("Component not configured. Call configure() first.");
        }

        Map<String, Object> props = config.getProperties();
        String topic = (String) props.remove("topic");

        if (topic == null) {
            throw new IllegalArgumentException("Property 'topic' is required for a KafkaProducer");
        }

        KafkaEndpointProducerBuilder builder = routeBuilder.kafka(topic);

        props.forEach(builder::doSetProperty);

        return processorDefinition.to(builder);
    }
}