package com.tsukoyachi.project;
public interface Consumer {
    String getName(); // Returns the component name (key for the hashmap)
    String getEndpoint();
    void configure(ComponentConfiguration config); // To configure the component
}