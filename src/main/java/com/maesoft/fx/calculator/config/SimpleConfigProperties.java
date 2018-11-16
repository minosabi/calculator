package com.maesoft.fx.calculator.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="fx.simple")
public class SimpleConfigProperties extends ConfigProperties {
}
