package org.spontaneous.service.tracking.configuration;

import org.spontaneous.service.tracking.general.service.api.ClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Client config.
 */
@Configuration
public class ClientConfig {

	/**
	 * Defines the androidClientProperties bean.
	 *
	 * @return the androidClientProperties bean
	 */
	@Bean
	@ConfigurationProperties(prefix = "spontaneous.client.android")
	public ClientProperties androidClientProperties() {
		return new ClientProperties();
	}

}
