package com.aegon.infrastructure;

import com.aegon.application.SectorRepository;
import com.aegon.application.TableManagementService;
import com.aegon.application.TableRepository;
import com.aegon.application.TableValidatorFactory;
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

	private final TableValidatorFactory validatorFactory;

	@Override
	public Mono<Table> addNewTable(AddNewTableRequest request) {
		validateRequests(request);
		final var sectorMono = sectorRepository.findByName(request.getName().getSectorName());
		return addTableWithSector(request, sectorMono);
	}

	@Override
	public Flux<Table> addNewTables(Set<AddNewTableRequest> requests) {
		validateRequests(requests.toArray(new AddNewTableRequest[0]));
		final var sectorNames = mapToSectorNames(requests);
		final Flux<Sector> sectorsFlux = findSectors(sectorNames);
		return addTablesLinkedWithSectors(requests, sectorsFlux);
	}

	@Override
	public Mono<Table> addCustomerToTable(AddCustomerToTableRequest request) {
		final var tableMono = tableRepository.findById(request.getTableId());
		return addCustomerToTable(request.getCustomerId(), tableMono);
	}

	private Mono<Table> addCustomerToTable(CustomerId customerId, Mono<Table> tableMono) {
		return tableMono.flatMap(table -> {
			table.addCustomer(customerId);
			return tableRepository.update(table);
		});
	}

	private Flux<Table> addTablesLinkedWithSectors(Set<AddNewTableRequest> requests, Flux<Sector> sectorsFlux) {
		return sectorsFlux.flatMap(sector -> Flux.fromIterable(requests)
				.filter(request -> isRequestWithSector(request, sector))
				.flatMap(request -> {
					final Table newTable = createNewTable(request, sector);
					return tableRepository.save(newTable);
				}));
	}

	private void validateRequests(AddNewTableRequest... requests) {
		for (AddNewTableRequest request : requests) {
			validatorFactory.of(request).validate();
		}
	}

	private Mono<Table> addTableWithSector(AddNewTableRequest request, Mono<Sector> sectorMono) {
		return sectorMono.flatMap(sector -> {
			final var table = createNewTable(request, sector);
			return tableRepository.save(table);
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
