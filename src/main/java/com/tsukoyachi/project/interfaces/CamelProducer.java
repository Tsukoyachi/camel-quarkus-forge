package com.tsukoyachi.project.interfaces;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.ProcessorDefinition;

public interface CamelProducer extends CamelComponent {
    
    /**
     * Configure la destination de la route sur le ProcessorDefinition fourni
     * @param routeBuilder Le RouteBuilder principal
     * @param processorDefinition Le ProcessorDefinition sur lequel ajouter la destination
     * @return ProcessorDefinition configuré avec la destination
     */
    ProcessorDefinition<?> configureTo(EndpointRouteBuilder routeBuilder, ProcessorDefinition<?> processorDefinition);
}