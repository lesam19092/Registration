package org.example.registration.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "duration")
@Data
public class DurationConfig {
    private Long shortTime;
    private Long longTime;
}
