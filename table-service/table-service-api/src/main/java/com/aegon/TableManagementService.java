package com.aegon;

import com.aegon.domain.Table;
import com.aegon.requests.AddCustomerToTableRequest;
import com.aegon.requests.AddNewTableRequest;
import java.util.Set;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TableManagementService {

	Mono<Table> addNewTable(AddNewTableRequest request);

	Flux<Table> addNewTables(Set<AddNewTableRequest> requests);

	Mono<Table> addCustomerToTable(AddCustomerToTableRequest request);

}
