package com.aegon;

import com.aegon.domain.Table;
import com.aegon.requests.AddCustomerToTableRequest;
import com.aegon.requests.AddNewTableRequest;
import reactor.core.publisher.Mono;

public interface TableManagementService {

	Mono<Table> addNewTable(AddNewTableRequest request);

	Mono<Table> addCustomerToTable(AddCustomerToTableRequest request);

}
