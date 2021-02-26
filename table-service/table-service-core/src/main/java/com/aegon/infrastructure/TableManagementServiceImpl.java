package com.aegon.infrastructure;

import com.aegon.SectorRepository;
import com.aegon.TableManagementService;
import com.aegon.TableRepository;
import com.aegon.domain.Sector;
import com.aegon.domain.SectorName;
import com.aegon.domain.Table;
import com.aegon.proxy.CustomerId;
import com.aegon.requests.AddCustomerToTableRequest;
import com.aegon.requests.AddNewTableRequest;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TableManagementServiceImpl implements TableManagementService {

	private final SectorRepository sectorRepository;

	private final TableRepository tableRepository;

	@Override
	public Mono<Table> addNewTable(AddNewTableRequest request) {
		final var sectorMono = sectorRepository.findByName(request.getName().getSectorName());
		return addTableWithSector(request, sectorMono);
	}

	@Override
	public Flux<Table> addNewTables(Set<AddNewTableRequest> requests) {
		final var sectorNames = mapToSectorNames(requests);
		final Flux<Sector> sectorsFlux = findSectors(sectorNames);
		return addTablesWithSectors(requests, sectorsFlux);
	}

	@Override
	public Mono<Table> addCustomerToTable(AddCustomerToTableRequest request) {
		final var tableMono = tableRepository.findById(request.getTableId());
		return addCustomerToTable(request.getCustomerId(), tableMono);
	}

	@Override
	public Mono<Table> removeCustomer(CustomerId customerId) {
		return tableRepository.findByCustomer(customerId)
				.flatMap(table -> {
					table.removeCustomer(customerId);
					return tableRepository.update(table);
				});
	}

	private Mono<Table> addCustomerToTable(CustomerId customerId, Mono<Table> tableMono) {
		return tableMono.flatMap(table -> addCustomerToTable(customerId, table));
	}

	private Mono<Table> addCustomerToTable(CustomerId customerId, Table table) {
		validateCustomerAddition(customerId, table);
		table.addCustomer(customerId);
		return tableRepository.update(table);
	}

	private void validateCustomerAddition(CustomerId customerId, Table table) {
		if (table.hasCustomer(customerId)) {
			throw TableAlreadyHasCustomer.err();
		}
		if (!table.isAnyPlaceLeft()) {
			throw TableNoMorePlacesLeft.err();
		}
	}

	private Flux<Table> addTablesWithSectors(Set<AddNewTableRequest> requests, Flux<Sector> sectorsFlux) {
		return sectorsFlux.flatMap(sector -> Flux.fromIterable(requests)
				.filter(request -> isRequestWithSector(request, sector))
				.flatMap(request -> {
					final Table newTable = createNewTable(request, sector);
					return tableRepository.save(newTable);
				}));
	}

	private Mono<Table> addTableWithSector(AddNewTableRequest request, Mono<Sector> sectorMono) {
		return sectorMono.flatMap(sector -> {
			final Mono<Table> tableMono = saveTable(request, sector);
			return assignTableToSector(tableMono, sector);
		});
	}

	private Mono<Table> saveTable(AddNewTableRequest request, Sector sector) {
		if (!sector.isAnyPlaceLeft()) {
			throw SectorNoMorePlacesLeft.err();
		}
		final var table = createNewTable(request, sector);
		final Mono<Table> tableMono = tableRepository.save(table);
		return tableMono;
	}

	private Mono<Table> assignTableToSector(Mono<Table> tableMono, Sector sector) {
		return tableMono.flatMap(table -> {
			sector.addTable(table);
			return sectorRepository.update(sector)
					.then(Mono.just(table));
		});
	}

	private Table createNewTable(AddNewTableRequest request, Sector sector) {
		return Table.builder()
				.name(request.getName())
				.customers(Collections.emptySet())
				.maxPlaces(request.getMaxPlaces())
				.sectorId(sector.getId())
				.build();
	}

	private boolean isRequestWithSector(AddNewTableRequest request, Sector sector) {
		return request.getName().getSectorName().equals(sector.getName());
	}

	private Flux<Sector> findSectors(Set<SectorName> sectorNames) {
		return Flux.fromIterable(sectorNames)
				.flatMap(sectorRepository::findByName);
	}

	private Set<SectorName> mapToSectorNames(Set<AddNewTableRequest> requests) {
		return requests.stream().map(req -> req.getName().getSectorName()).collect(Collectors.toSet());
	}
}
