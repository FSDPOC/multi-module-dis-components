package com.db.camunda.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("camunda-service")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Configuration {

	private String dmnFilePath;
}
