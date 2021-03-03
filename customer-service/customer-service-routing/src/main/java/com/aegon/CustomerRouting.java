package com.aegon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class CustomerRouting {

	@Bean
	public RouterFunction<ServerResponse> routerFunction(CustomerRoutingHandler routingHandler) {
		return nest(
				path("/customer"),
				route(GET("").and(accept(MediaType.APPLICATION_JSON)), routingHandler::getCustomerByName)
						.andRoute(GET("/c}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::getCustomerByName)
		);
	}

}
