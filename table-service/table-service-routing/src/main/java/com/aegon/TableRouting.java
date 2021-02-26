package com.aegon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
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
						.andRoute(DELETE("/{tableId}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::deleteTableById)
						.andRoute(DELETE("/name/{tableName}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::deleteTableByName)
						.andRoute(POST("/generate").and(accept(MediaType.MULTIPART_FORM_DATA)), routingHandler::generateTables)
						.andRoute(GET("/{tableName}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::findTableByName)

						.andNest(
								path("/sectors"),
								route(POST("").and(accept(MediaType.APPLICATION_JSON)), routingHandler::addSector)
										.andRoute(GET("/{sectorName}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::findSectorTables)
										.andRoute(DELETE("/{sectorId}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::deleteSectorById)
										.andRoute(DELETE("name/{sectorName}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::deleteSectorByName)
										.andRoute(GET("/sector/{sectorName}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::findSectorByName)

						)

						.andNest(
								path("/customer"),
								route(POST("").and(accept(MediaType.APPLICATION_JSON)), routingHandler::addCustomerToTable)
										.andRoute(GET("/{customerId}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::findTableByCustomer)
										.andRoute(DELETE("/{customerId}").and(accept(MediaType.APPLICATION_JSON)), routingHandler::removeCustomerFromTable)
						)
		);
	}

}
