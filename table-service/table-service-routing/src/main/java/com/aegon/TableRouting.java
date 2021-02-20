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
public class TableRouting {

	@Bean
	public RouterFunction<ServerResponse> routerFunction(TableRoutingHandler routingHandler) {
		return nest(
				path("/tables"),
				route(POST("").and(accept(MediaType.APPLICATION_JSON)), routingHandler::addTable)
						.andRoute(POST("/generate").and(accept(MediaType.APPLICATION_JSON)), routingHandler::generateDefaultTables)
						.andRoute(POST("/file").and(accept(MediaType.MULTIPART_FORM_DATA)), routingHandler::generateTables)
						.andRoute(POST("/customer").and(accept(MediaType.APPLICATION_JSON)), routingHandler::addCustomer)
						.andRoute(GET("/customer/{customerId}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::findByCustomer)
		)
				.andNest(
						path("/sectors"),
						route(GET("/sector/{sectorName}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::getSector)
				);
	}

}
