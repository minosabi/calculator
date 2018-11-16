package com.maesoft.fx.calculator.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Configuration
@PropertySource("classpath:properties/fx.properties")
public class ConfigProperties {
    @NotNull
    @NotBlank
    private String fxmlLocation;

    @NotNull
    @NotBlank
    private String cssLocation;
}
