package com.aegon.application.generator;

import com.aegon.SectorManagementService;
import com.aegon.SectorRepository;
import com.aegon.TableManagementService;
import com.aegon.TableRepository;
import com.aegon.application.SupportedFileExtension;
import com.aegon.application.TableGenerator;
import com.aegon.domain.Sector;
import com.aegon.domain.SectorId;
import com.aegon.domain.SectorName;
import com.aegon.domain.Table;
import com.aegon.requests.AddNewSectorRequest;
import com.aegon.requests.AddNewTableRequest;
import com.aegon.util.lang.Preconditions;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TableGeneratorImpl implements TableGenerator {

	@Value("classpath:tables/DefaultTables.csv")
	private Resource resourceFile;

	private final TableManagementService tableManagementService;

	private final SectorManagementService sectorManagementService;

	private final SectorRepository sectorRepository;

	private final TableRepository tableRepository;

	public TableGeneratorImpl(TableManagementService tableManagementService,
			SectorManagementService sectorManagementService,
			SectorRepository sectorRepository, TableRepository tableRepository) {
		this.tableManagementService = Preconditions.requireNonNull(tableManagementService);
		this.sectorManagementService = Preconditions.requireNonNull(sectorManagementService);
		this.sectorRepository = Preconditions.requireNonNull(sectorRepository);
		this.tableRepository = Preconditions.requireNonNull(tableRepository);
	}

	@Override
	public Flux<Table> generate(DataBuffer dataBuffer, SupportedFileExtension extension) {
		final List<AddNewTableRequest> requestFlux = DataBufferParserFactory.create(dataBuffer, extension).parse();
		return saveTablesAndSectors(requestFlux);
	}

	@Override
	public Flux<Table> generateDefault() {
		final Flux<DataBuffer> read = takeResource(resourceFile);
		return read.flatMap(dataBuffer -> generate(dataBuffer, SupportedFileExtension.CSV));
	}

	private Flux<DataBuffer> takeResource(Resource resource) {
		return DataBufferUtils.read(resource,
				new DefaultDataBufferFactory(),
				4096);
	}

	private Flux<Table> saveTablesAndSectors(List<AddNewTableRequest> requests) {
		final Flux<Sector> sectorFlux = saveSectorsWithoutTableIds(requests);
		final Flux<Table> tableFlux = saveTablesWithSectorIds(requests, sectorFlux);
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

	private Flux<Table> saveTablesWithSectorIds(List<AddNewTableRequest> requestFlux, Flux<Sector> sectorFlux) {
		return sectorFlux.collectList()
				.flatMapMany(request -> saveTablesWithSectorIds(requestFlux));
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

	private Flux<Sector> saveSectorsWithoutTableIds(List<AddNewTableRequest> tableRequests) {
		final Set<AddNewSectorRequest> sectorRequests = mapToSectorRequests(tableRequests);
		return sectorManagementService.addNewSectors(sectorRequests);
	}

	private Set<AddNewSectorRequest> mapToSectorRequests(List<AddNewTableRequest> tableRequests) {
		final Set<AddNewSectorRequest> result = new HashSet<>();
		final Map<SectorName, List<AddNewTableRequest>> requestsPerSectorName = groupingBySectorName(tableRequests);
		requestsPerSectorName.keySet()
				.forEach(name -> result.add(new AddNewSectorRequest(name, requestsPerSectorName.get(name).size())));
		return result;
	}
}
