package com.aegon.infrastructure;

import com.aegon.TableRepository;
import com.aegon.domain.MongoKvTableDocument;
import com.aegon.domain.MongoSectorDocument;
import com.aegon.domain.SectorName;
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

	private final MongoTableRepository mongoTableRepository;

	private final MongoSectorRepository mongoSectorRepository;

	private final TableMapper mapper = TableMapper.getInstance();

	@Override
	public Mono<Table> findByName(TableName name) {
		return mongoTableRepository.findByName(name.getStringValue())
				.map(mapper::map)
				.switchIfEmpty(Mono.error(TableRepositoryException.of(String.format("Table with name %s not found", name.getStringValue()))));
	}

	@Override
	public Mono<Table> findByCustomer(CustomerId customerId) {
		return mongoTableRepository.findByCustomerId(customerId.getInternal())
				.map((mapper::map))
				.switchIfEmpty(Mono.error(TableRepositoryException.of(String.format("Table with customer id %s not found", customerId.getInternal()))));
	}

	@Override
	public Mono<TableId> deleteByName(TableName name) {
		return mongoTableRepository.findByName(name.getStringValue())
				.flatMap(this::removeTableFromAssignedSector)
				.switchIfEmpty(Mono.error(TableRepositoryException.of(String.format("Table with name %s could not be deleted", name.getStringValue()))));
	}

	@Override
	public Mono<TableId> delete(TableId id) {
		return mongoTableRepository.findById(id.getInternal())
				.flatMap(this::removeTableFromAssignedSector)
				.switchIfEmpty(Mono.error(TableRepositoryException.of(String.format("Table with id %s could not be deleted", id.getInternal()))));
	}

	@Override
	public Flux<Table> findBySectorName(SectorName sectorName) {
		return mongoSectorRepository.findByName(sectorName.getInternal())
				.flatMapIterable(MongoSectorDocument::getTableIds)
				.flatMap(mongoTableRepository::findById)
				.map((mapper::map))
				.switchIfEmpty(Mono.error(TableRepositoryException.of(String.format("Tables from sector name %s not found", sectorName.getInternal()))));
	}

	@Override
	public Mono<Table> findById(TableId id) {
		return mongoTableRepository.findById(id.getInternal())
				.map(mapper::map)
				.switchIfEmpty(Mono.error(TableRepositoryException.of(String.format("Table with id %s not found", id.getInternal()))));
	}

	@Override
	public Mono<Table> save(Table table) {
		final String stringValue = table.getName().getStringValue();
		return mongoTableRepository.save(
				MongoKvTableDocument.builder()
						.name(stringValue)
						.maxPlaces(table.getMaxPlaces())
						.customerIds(table.getCustomers().stream().map(CustomerId::getInternal).collect(Collectors.toSet()))
						.sectorId(table.getSectorId().getInternal())
						.build())
				.map(mapper::map)
				.doOnError(err -> {
					throw TableAlreadyExistsException.error();
				});
	}

	@Override
	public Mono<Table> update(Table table) {
		return mongoTableRepository.findById(table.getId().getInternal())
				.flatMap(kvTable -> {
					kvTable.update(table);
					return mongoTableRepository.save(kvTable)
							.map(mapper::map);
				})
				.switchIfEmpty(Mono.error(TableRepositoryException.of(String.format("Table with id %s could not be updated", table.getId().getInternal()))));
	}

	private Mono<TableId> removeTableFromAssignedSector(MongoKvTableDocument mongoTable) {
		return mongoSectorRepository.findById(mongoTable.getSectorId())
				.flatMap(mongoSector -> {
					mongoSector.removeTable(mongoTable.getId());
					return mongoSectorRepository.save(mongoSector);
				})
				.flatMap(mongoSector -> mongoTableRepository.delete(mongoTable))
				.then(Mono.just(TableId.valueOf(mongoTable.getId())));
	}
}
