package com.aegon;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import static com.aegon.util.spring.webflux.ResponseResolver.toMonoServerResponse;

@Component
@RequiredArgsConstructor
public class CustomerRoutingHandler {

	Mono<ServerResponse> getCustomerByName(ServerRequest request) {
		return toMonoServerResponse(Mono.just("Hello World!"));
	}
}
