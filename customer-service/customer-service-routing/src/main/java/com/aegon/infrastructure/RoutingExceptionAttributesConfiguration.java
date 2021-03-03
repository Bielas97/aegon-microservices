package com.aegon.infrastructure;

import com.aegon.util.spring.webflux.exception.BaseExceptionErrorAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RoutingExceptionAttributesConfiguration {

	@Bean
	public BaseExceptionErrorAttributes baseExceptionErrorAttributes() {
		return BaseExceptionErrorAttributes.withErrorLogger(log::error);
	}

}
