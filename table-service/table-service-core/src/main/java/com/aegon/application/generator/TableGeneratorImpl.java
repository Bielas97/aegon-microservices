package com.aegon.application.generator;

import com.aegon.application.SectorManagementService;
import com.aegon.application.SectorRepository;
import com.aegon.application.TableGenerator;
import com.aegon.application.TableManagementService;
import com.aegon.application.TableRepository;
import com.aegon.domain.Sector;
import com.aegon.domain.SectorId;
import com.aegon.domain.SectorName;
import com.aegon.domain.Table;
import com.aegon.requests.AddNewSectorRequest;
import com.aegon.requests.AddNewTableRequest;
import com.aegon.util.lang.Preconditions;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TableGeneratorImpl implements TableGenerator {

	private final FileParserFactory fileParserFactory;

	private final TableManagementService tableManagementService;

	private final SectorManagementService sectorManagementService;

	private final SectorRepository sectorRepository;

	private final TableRepository tableRepository;

	public TableGeneratorImpl(FileParserFactory fileParserFactory, TableManagementService tableManagementService,
			SectorManagementService sectorManagementService,
			SectorRepository sectorRepository, TableRepository tableRepository) {
		this.fileParserFactory = Preconditions.requireNonNull(fileParserFactory);
		this.tableManagementService = Preconditions.requireNonNull(tableManagementService);
		this.sectorManagementService = Preconditions.requireNonNull(sectorManagementService);
		this.sectorRepository = Preconditions.requireNonNull(sectorRepository);
		this.tableRepository = Preconditions.requireNonNull(tableRepository);
	}

	@Override
	public Flux<Table> generate(File file) {
		final Flux<AddNewTableRequest> tableRequestFlux = fileParserFactory.create(file).parse();
		return saveTablesAndSectors(tableRequestFlux);
	}

	@Override
	public Flux<Table> generateDefault() {
		final Flux<AddNewTableRequest> tableRequestFlux = fileParserFactory.defaultParser().parse();
		return saveTablesAndSectors(tableRequestFlux);

	}

	private Flux<Table> saveTablesAndSectors(Flux<AddNewTableRequest> requestFlux) {
		final Flux<Sector> sectorFlux = saveSectorsWithoutTableIds(requestFlux);
		final Flux<Table> tableFlux = saveTablesWithSectorIds(requestFlux, sectorFlux);
		final Flux<Sector> sectorWithTableIdsFlux = saveTableIdsInSectors(tableFlux);
		return extractTablesFromSectors(sectorWithTableIdsFlux);
	}

	private Flux<Table> extractTablesFromSectors(Flux<Sector> sectorWithTableIdsFlux) {
		return sectorWithTableIdsFlux.flatMap(sector -> Flux.fromIterable(sector.getTableIds())
				.flatMap(tableRepository::findById));
	}

	private Flux<Sector> saveTableIdsInSectors(Flux<Table> tableFlux) {
		return tableFlux.collectList()
				.flatMapMany(this::saveTableIdsInSectors);
	}

	private Flux<Sector> saveTableIdsInSectors(List<Table> tables) {
		final Map<SectorId, List<Table>> tablesPerSectorId = groupBySectorId(tables);
		return Flux.fromIterable(tablesPerSectorId.keySet())
				.flatMap(sectorId -> sectorRepository.findById(sectorId).flatMap(sector -> {
					sector.addTables(tablesPerSectorId.get(sectorId));
					return sectorRepository.update(sector);
				}));
	}

	private Map<SectorId, List<Table>> groupBySectorId(List<Table> tables) {
		return tables.stream().collect(Collectors.groupingBy(Table::getSectorId));
	}

	private Flux<Table> saveTablesWithSectorIds(Flux<AddNewTableRequest> requestFlux, Flux<Sector> sectorFlux) {
		return requestFlux.collectList().zipWith(sectorFlux.collectList())
				.flatMapMany(tuple -> saveTablesWithSectorIds(tuple.getT1()));
	}

	private Flux<Table> saveTablesWithSectorIds(List<AddNewTableRequest> tableRequests) {
		final var tablesReqPerSector = groupingBySectorName(tableRequests);
		return Flux.fromIterable(tablesReqPerSector.keySet())
				.flatMap(sectorName -> sectorRepository
						.findByName(sectorName)
						.flatMapMany(sector -> tableManagementService.addNewTables(new HashSet<>(tablesReqPerSector.get(sectorName))))
				);
	}

	private Map<SectorName, List<AddNewTableRequest>> groupingBySectorName(List<AddNewTableRequest> tableRequests) {
		return tableRequests.stream()
				.collect(Collectors.groupingBy(table -> table.getName().getSectorName()));
	}

	private Flux<Sector> saveSectorsWithoutTableIds(Flux<AddNewTableRequest> requestFlux) {
		return requestFlux.collectList().flatMapMany(tableRequests -> {
			final Set<AddNewSectorRequest> sectorRequests = mapToSectorRequests(tableRequests);
			return sectorManagementService.addNewSectors(sectorRequests);
		});
	}

	private Set<AddNewSectorRequest> mapToSectorRequests(List<AddNewTableRequest> tableRequests) {
		final Set<AddNewSectorRequest> result = new HashSet<>();
		final Map<SectorName, List<AddNewTableRequest>> requestsPerSectorName = groupingBySectorName(tableRequests);
		requestsPerSectorName.keySet()
				.forEach(name -> result.add(new AddNewSectorRequest(name, requestsPerSectorName.get(name).size())));
		return result;
	}
}
