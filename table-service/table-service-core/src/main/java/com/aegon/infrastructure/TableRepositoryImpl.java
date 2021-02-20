package com.aegon.infrastructure;

import com.aegon.TableRepository;
import com.aegon.domain.MongoKvTableDocument;
import com.aegon.domain.Table;
import com.aegon.domain.TableId;
import com.aegon.domain.TableName;
import com.aegon.proxy.CustomerId;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TableRepositoryImpl implements TableRepository {

	private final MongoTableRepository mongoRepository;

	private final TableMapper mapper = TableMapper.getInstance();

	@Override
	public Mono<Table> findByName(TableName name) {
		return mongoRepository.findByName(name.getStringValue())
				.map(mapper::map);
	}

	@Override
	public Mono<Table> findByCustomer(CustomerId customerId) {
		return mongoRepository.findByCustomerId(customerId.getInternal())
				.map((mapper::map))
				.switchIfEmpty(Mono.error(TableRepositoryException.of("Table not found")));
	}

	@Override
	public Mono<Table> findById(TableId id) {
		return mongoRepository.findById(id.getInternal())
				.map(mapper::map)
				.switchIfEmpty(Mono.error(TableRepositoryException.of("Table not found")));
	}

	@Override
	public Mono<Table> save(Table table) {
		final String stringValue = table.getName().getStringValue();
		return mongoRepository.save(MongoKvTableDocument.builder()
				.name(stringValue)
				.maxPlaces(table.getMaxPlaces())
				.customerIds(table.getCustomers().stream().map(CustomerId::getInternal).collect(Collectors.toSet()))
				.sectorId(table.getSectorId().getInternal())
				.build())
				.map(mapper::map);
	}

	@Override
	public Mono<TableId> delete(TableId id) {
		return mongoRepository.deleteById(id.getInternal())
				.flatMap(unused -> Mono.just(id));
	}

	@Override
	public Mono<Table> update(Table table) {
		return mongoRepository.findById(table.getId().getInternal())
				.flatMap(kvTable -> {
					kvTable.update(table);
					return mongoRepository.save(kvTable)
							.map(mapper::map);
				});
	}
}
