package com.tsukoyachi.project;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class TestRoute extends RouteBuilder {
    
    @Inject
    RouteConfigurationService routeConfigService;
    
    @Inject
    ComponentFactory componentFactory;

    @Override
    public void configure() throws Exception {
        // Load route configurations from YAML
        List<RouteConfiguration> routes = routeConfigService.loadRouteConfigurations();
        
        for (RouteConfiguration routeConfig : routes) {
            createDynamicRoute(routeConfig);
        }
    }
    
    private void createDynamicRoute(RouteConfiguration config) throws Exception {
        // Create source consumer
        Consumer sourceConsumer = componentFactory.createConsumer(config.getSource());
        
        // Create destination producer  
        Producer destinationProducer = componentFactory.createProducer(config.getDestination());
        
        // Create dynamic route
        RouteDefinition route = from(sourceConsumer.getEndpoint())
            .routeId(config.getId())
            .log("Processing message in route: " + config.getId());
        
        // Apply processors if defined
        if (config.getProcessors() != null) {
            for (ProcessorConfiguration procConfig : config.getProcessors()) {
                org.apache.camel.Processor processor = componentFactory.createProcessor(procConfig);
                route = route.process(processor);
            }
        }
        
        // End with producer
        route.to(destinationProducer.getEndpoint());
    }
}
