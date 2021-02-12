package com.aegon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouting {

	@Bean
	public RouterFunction<ServerResponse> routerFunction(UserRoutingHandler routingHandler) {
		return nest(
				path("/users"),
				route(POST("/register").and(accept(MediaType.APPLICATION_JSON)), routingHandler::register)
						.andRoute(POST("/{id}/password").and(accept(MediaType.APPLICATION_JSON)), routingHandler::changePassword)
						.andNest(
								path("/find"),
								route(GET("/username/{username}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::findByUsername)
										.andRoute(GET("/id/{id}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::findById)
						)
		);
	}

}
