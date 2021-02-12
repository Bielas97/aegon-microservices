package com.aegon.application;

import com.aegon.TableRepository;
import com.aegon.domain.MongoKvTableDocument;
import com.aegon.domain.Table;
import com.aegon.domain.TableId;
import com.aegon.domain.TableName;
import com.aegon.infrastructure.MongoTableRepository;
import com.aegon.proxy.CustomerId;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TableRepositoryImpl implements TableRepository {

	private final MongoTableRepository mongoRepository;

	@Override
	public Mono<Table> findByName(TableName name) {
		return mongoRepository.findByName(name.getInternal())
				.map(this::map);
	}

	@Override
	public Mono<Table> findByCustomer(CustomerId customerId) {
		return mongoRepository.findByCustomerId(customerId.getInternal())
				.map((this::map));
	}

	@Override
	public Mono<Table> findById(TableId id) {
		return mongoRepository.findById(id.getInternal())
				.map(this::map);
	}

	@Override
	public Mono<Table> save(Table table) {
		return mongoRepository.save(MongoKvTableDocument.builder()
				.name(table.getName().getInternal())
				.maxPlaces(table.getMaxPlaces())
				.customerIds(table.getCustomers().stream().map(CustomerId::getInternal).collect(Collectors.toSet()))
				.build())
				.map(this::map);
	}

	@Override
	public Mono<TableId> delete(TableId id) {
		return null;
	}

	@Override
	public Mono<Table> update(Table table) {
		return mongoRepository.save(map(table))
				.map(this::map);
	}

	private Table map(MongoKvTableDocument document) {
		return Table.builder()
				.id(TableId.valueOf(document.getId()))
				.name(TableName.valueOf(document.getName()))
				.maxPlaces(document.getMaxPlaces())
				.customers(document.getCustomerIds().stream().map(CustomerId::valueOf).collect(Collectors.toSet()))
				.build();
	}

	private MongoKvTableDocument map(Table table) {
		return MongoKvTableDocument.builder()
				.id(table.getId().getInternal())
				.name(table.getName().getInternal())
				.maxPlaces(table.getMaxPlaces())
				.customerIds(table.getCustomers().stream().map(CustomerId::getInternal).collect(Collectors.toSet()))
				.build();
	}
}
