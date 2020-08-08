package com.example.fixed;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class HelloProperties {
    @Value("${hello.log}")
    private boolean log;

    public boolean isLog() {
        return log;
    }
}
