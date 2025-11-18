package com.tsukoyachi.project.interfaces;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.RouteDefinition;

public interface CamelConsumer extends CamelComponent {
    
    /**
     * Configure et retourne un RouteDefinition pour initier une route
     * @param routeBuilder Le RouteBuilder principal
     * @param routeId L'ID de la route pour identification
     * @return RouteDefinition configurée pour démarrer une route
     */
    RouteDefinition configureFrom(EndpointRouteBuilder routeBuilder, String routeId);
}