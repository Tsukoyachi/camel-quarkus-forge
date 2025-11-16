package com.tsukoyachi.project.interfaces;

import com.tsukoyachi.project.ComponentConfiguration;

public interface CamelComponent {
    String getName();
    String getEndpoint();
    void configure(ComponentConfiguration config);
}
