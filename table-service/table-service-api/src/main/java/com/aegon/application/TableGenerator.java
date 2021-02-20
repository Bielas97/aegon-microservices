package com.aegon.application;

import com.aegon.domain.Table;
import java.io.File;
import reactor.core.publisher.Flux;

public interface TableGenerator {

	Flux<Table> generate(File file);

	Flux<Table> generateDefault();

}
