package com.aegon.infrastructure;

import com.aegon.application.SectorManagementService;
import com.aegon.application.SectorRepository;
import com.aegon.application.TableRepository;
import com.aegon.domain.Sector;
import com.aegon.domain.TableId;
import com.aegon.requests.AddNewSectorRequest;
import com.aegon.requests.AddTableToSectorRequest;
import com.aegon.requests.AddTablesToSectorRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SectorManagementServiceImpl implements SectorManagementService {

	private final SectorRepository sectorRepository;

	private final TableRepository tableRepository;

	@Override
	public Mono<Sector> addNewSector(AddNewSectorRequest request) {
		return sectorRepository.save(Sector.builder()
				.name(request.name())
				.maxTables(request.maxTables())
				.build());
	}

	@Override
	public Flux<Sector> addNewSectors(Collection<AddNewSectorRequest> requests) {
		final Set<Sector> sectors = requests.stream().map(request -> Sector.builder()
				.name(request.name())
				.maxTables(request.maxTables())
				.tables(new HashSet<>())
				.build())
				.collect(Collectors.toSet());
		return sectorRepository.saveAll(sectors);
	}

	@Override
	public Mono<Sector> addTableToSector(AddTableToSectorRequest request) {
		return sectorRepository.findById(request.sectorId())
				.flatMap(sector -> addTableTo(sector, request.tableId()));
	}

	@Override
	public Mono<Sector> addTablesToSector(AddTablesToSectorRequest request) {
		return sectorRepository.findById(request.sectorId())
				.flatMap(sector -> addTablesTo(sector, request.tables()));
	}

	private Mono<Sector> addTableTo(Sector sector, TableId tableId) {
		return tableRepository.findById(tableId)
				.flatMap(table -> {
					sector.addTable(table);
					return sectorRepository.update(sector);
				});
	}

	private Mono<Sector> addTablesTo(Sector sector, Set<TableId> tableIds) {
		return Flux.fromIterable(tableIds)
				.flatMap(id -> addTableTo(sector, id))
				.last();
	}
}
