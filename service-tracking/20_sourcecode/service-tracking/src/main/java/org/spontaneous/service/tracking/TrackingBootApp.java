package org.spontaneous.service.tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableDiscoveryClient
@EnableOAuth2Sso
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TrackingBootApp {

	public static void main(String[] args) {
		SpringApplication.run(TrackingBootApp.class, args);
	}

}
