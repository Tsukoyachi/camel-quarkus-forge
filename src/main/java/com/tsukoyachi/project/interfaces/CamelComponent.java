package com.tsukoyachi.project.interfaces;

import com.tsukoyachi.project.ComponentConfiguration;

public interface CamelComponent {
    String getName();
    void configure(ComponentConfiguration config);
}
