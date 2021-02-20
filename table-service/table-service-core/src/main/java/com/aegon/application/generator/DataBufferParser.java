package com.aegon.application.generator;

import com.aegon.requests.AddNewTableRequest;
import java.util.List;

@FunctionalInterface
interface DataBufferParser {

	List<AddNewTableRequest> parse();
}
