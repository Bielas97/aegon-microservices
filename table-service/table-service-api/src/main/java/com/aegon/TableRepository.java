package com.aegon;

import com.aegon.domain.Table;
import com.aegon.domain.TableId;
import com.aegon.domain.TableName;
import com.aegon.proxy.CustomerId;
import com.aegon.util.lang.DomainReactiveRepository;
import reactor.core.publisher.Mono;

public interface TableRepository extends DomainReactiveRepository<TableId, Table> {

	Mono<Table> findByName(TableName name);

	Mono<Table> findByCustomer(CustomerId customerId);

}
