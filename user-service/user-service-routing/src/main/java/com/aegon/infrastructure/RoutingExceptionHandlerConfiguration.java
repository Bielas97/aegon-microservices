package com.aegon.infrastructure;

import com.aegon.util.spring.webflux.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;

@Configuration
@Order(-2)
@RequiredArgsConstructor
public class RoutingExceptionHandlerConfiguration {

	private final ErrorAttributes errorAttributes;

	private final WebProperties.Resources resources;

	private final ApplicationContext applicationContext;

	private final ServerCodecConfigurer serverCodecConfigurer;

	@Bean
	public GlobalExceptionHandler globalExceptionHandler() {
		return new GlobalExceptionHandler(errorAttributes, resources, applicationContext, serverCodecConfigurer);
	}

}
