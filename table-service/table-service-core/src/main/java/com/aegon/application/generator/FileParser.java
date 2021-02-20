package com.aegon.application.generator;

import com.aegon.requests.AddNewTableRequest;
import reactor.core.publisher.Flux;

@FunctionalInterface
interface FileParser {

	Flux<AddNewTableRequest> parse();
}
