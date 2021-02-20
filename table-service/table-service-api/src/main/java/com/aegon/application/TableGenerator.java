package com.aegon.application;

import com.aegon.domain.Table;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;

public interface TableGenerator {

	Flux<Table> generate(DataBuffer dataBuffer, SupportedFileExtension extension);

	Flux<Table> generateDefault();

}
