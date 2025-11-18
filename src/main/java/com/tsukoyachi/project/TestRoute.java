package com.tsukoyachi.project;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.RouteDefinition;

import com.tsukoyachi.project.config.RouteConfigurationMapper;
import com.tsukoyachi.project.interfaces.CamelConsumer;
import com.tsukoyachi.project.interfaces.CamelProducer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class TestRoute extends EndpointRouteBuilder {
    
    @Inject
    RouteConfigurationMapper routeConfigurationMapper;
    
    @Inject
    ComponentFactory componentFactory;

    @Override
    public void configure() throws Exception {
        // Load route configurations from YAML
        List<RouteConfiguration> routes = routeConfigurationMapper.mapToRouteConfigurations();
        
        for (RouteConfiguration routeConfig : routes) {
            createDynamicRoute(routeConfig);
        }
    }
    
    private void createDynamicRoute(RouteConfiguration config) throws Exception {
        // Create source consumer
        CamelConsumer sourceConsumer = componentFactory.createConsumer(config.getSource());
        
        // Create destination producer  
        CamelProducer destinationProducer = componentFactory.createProducer(config.getDestination());
        
        // Create dynamic route using the new configureFrom method
        RouteDefinition route = sourceConsumer.configureFrom(this, config.getId())
            .log("Processing message in route: " + config.getId());
        
        // Apply processors if defined
        if (config.getProcessors() != null) {
            for (ProcessorConfiguration procConfig : config.getProcessors()) {
                org.apache.camel.Processor processor = componentFactory.createProcessor(procConfig);
                route = route.process(processor);
            }
        }
        
        // End with producer using the new configureTo method
        destinationProducer.configureTo(this, route);
    }
}
