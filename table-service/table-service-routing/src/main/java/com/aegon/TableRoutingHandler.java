package com.aegon;

import com.aegon.application.SupportedFileExtension;
import com.aegon.application.TableGenerator;
import com.aegon.domain.AddCustomerToTableRequestDTO;
import com.aegon.domain.AddNewSectorRequestDTO;
import com.aegon.domain.AddNewTableRequestDTO;
import com.aegon.domain.Sector;
import com.aegon.domain.SectorDTO;
import com.aegon.domain.SectorId;
import com.aegon.domain.SectorName;
import com.aegon.domain.Table;
import com.aegon.domain.TableDTO;
import com.aegon.domain.TableId;
import com.aegon.domain.TableName;
import com.aegon.proxy.CustomerId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static com.aegon.util.spring.webflux.ResponseResolver.toMonoServerResponse;

@Component
@RequiredArgsConstructor
class TableRoutingHandler {

	private final TableManagementService tableManagementService;

	private final SectorManagementService sectorManagementService;

	private final TableRepository tableRepository;

	private final SectorRepository sectorRepository;

	private final TableGenerator tableGenerator;

	Mono<ServerResponse> addTable(ServerRequest serverRequest) {
		final var addNewTableRequestMono = serverRequest.bodyToMono(AddNewTableRequestDTO.class);
		return toMonoServerResponse(addNewTableRequestMono.flatMap(request -> {
			final var tableMono = tableManagementService.addNewTable(request.toDomain());
			return tableMono.map(TableDTO::from);
		}));
	}

	Mono<ServerResponse> addSector(ServerRequest serverRequest) {
		final var addNewSectorRequestMono = serverRequest.bodyToMono(AddNewSectorRequestDTO.class);
		return toMonoServerResponse(addNewSectorRequestMono.flatMap(request -> {
			final var sectorMono = sectorManagementService.addNewSector(request.toDomain());
			return sectorMono.map(SectorDTO::from);
		}));
	}

	Mono<ServerResponse> findSectorTables(ServerRequest request) {
		final var sectorName = SectorName.valueOf(request.pathVariable("sectorName"));
		final var tablesFlux = tableRepository.findBySectorName(sectorName).map(TableDTO::from);
		return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
				.body(tablesFlux, Table.class);
	}

	Mono<ServerResponse> addCustomerToTable(ServerRequest serverRequest) {
		final var addCustomerToTableRequestDTO = serverRequest.bodyToMono(AddCustomerToTableRequestDTO.class);
		return toMonoServerResponse(addCustomerToTableRequestDTO.flatMap(request -> {
			final var tableMono = tableManagementService.addCustomerToTable(request.toDomain());
			return tableMono.map(TableDTO::from);
		}));
	}

	Mono<ServerResponse> findTableByCustomer(ServerRequest request) {
		final var customerId = CustomerId.valueOf(request.pathVariable("customerId"));
		final var tableMono = tableRepository.findByCustomer(customerId).map(TableDTO::from);
		return toMonoServerResponse(tableMono);
	}

	Mono<ServerResponse> findTableByName(ServerRequest request) {
		final var name = TableName.from(request.pathVariable("tableName"));
		final var tableMono = tableRepository.findByName(name).map(TableDTO::from);
		return toMonoServerResponse(tableMono);
	}

	Mono<ServerResponse> findSectorByName(ServerRequest request) {
		final var name = SectorName.valueOf(request.pathVariable("sectorName"));
		final Mono<Sector> sectorMono = sectorRepository.findByName(name);
		return toMonoServerResponse(sectorMono.map(SectorDTO::from));
	}

	Mono<ServerResponse> deleteTableById(ServerRequest request) {
		final var id = TableId.valueOf(request.pathVariable("tableId"));
		final var idMono = tableRepository.delete(id);
		return toMonoServerResponse(idMono);
	}

	Mono<ServerResponse> deleteSectorById(ServerRequest request) {
		final var id = SectorId.valueOf(request.pathVariable("sectorId"));
		final var idMono = sectorManagementService.delete(id);
		return toMonoServerResponse(idMono);
	}

	Mono<ServerResponse> deleteSectorByName(ServerRequest request) {
		final var name = SectorName.valueOf(request.pathVariable("sectorName"));
		final Mono<SectorId> sectorIdMono = sectorManagementService.deleteByName(name);
		return toMonoServerResponse(sectorIdMono);
	}

	Mono<ServerResponse> removeCustomerFromTable(ServerRequest request) {
		final var customerId = CustomerId.valueOf(request.pathVariable("customerId"));
		final var tableMono = tableManagementService.removeCustomer(customerId).map(TableDTO::from);
		return toMonoServerResponse(tableMono);
	}

	Mono<ServerResponse> deleteTableByName(ServerRequest request) {
		final var tableNameString = request.pathVariable("tableName");
		final var tableIdMono = tableRepository.deleteByName(TableName.from(tableNameString));
		return toMonoServerResponse(tableIdMono);
	}

	Mono<ServerResponse> generateTables(ServerRequest request) {
		final Boolean isDefaultGeneration = request.queryParam("default")
				.map(Boolean::valueOf)
				.orElse(Boolean.FALSE);
		final var tablesFlux = isDefaultGeneration ? tableGenerator.generateDefault() : generate(request);
		return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
				.body(tablesFlux.map(TableDTO::from), Table.class);
	}

	private Flux<Table> generate(ServerRequest request) {
		return request.multipartData()
				.flatMapIterable(partsMap -> partsMap.get("file"))
				.map(part -> (FilePart) part)
				.flatMap(this::generateTables);
	}

	private Flux<Table> generateTables(FilePart part) {
		final String filename = part.filename();
		final SupportedFileExtension extension = SupportedFileExtension.from(filename.substring(filename.lastIndexOf(".") + 1));
		return part.content().flatMap(dataBuffer -> tableGenerator.generate(dataBuffer, extension));
	}
}
