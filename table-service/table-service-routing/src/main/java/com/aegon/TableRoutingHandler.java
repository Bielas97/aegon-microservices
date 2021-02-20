package com.aegon;

import com.aegon.application.SectorRepository;
import com.aegon.application.TableGenerator;
import com.aegon.application.TableManagementService;
import com.aegon.application.TableRepository;
import com.aegon.domain.AddCustomerToTableRequestDTO;
import com.aegon.domain.AddNewTableRequestDTO;
import com.aegon.domain.Sector;
import com.aegon.domain.SectorName;
import com.aegon.domain.Table;
import com.aegon.domain.TableDTO;
import com.aegon.domain.TableId;
import com.aegon.domain.TableName;
import com.aegon.proxy.CustomerId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static com.aegon.util.spring.webflux.ResponseResolver.toMonoServerResponse;

@Component
@RequiredArgsConstructor
public class TableRoutingHandler {

	private final TableManagementService tableManagementService;

	private final TableRepository tableRepository;

	private final SectorRepository sectorRepository;

	private final TableGenerator generator;

	public Mono<ServerResponse> addTable(ServerRequest serverRequest) {
		final var addNewTableRequestMono = serverRequest.bodyToMono(AddNewTableRequestDTO.class);
		return toMonoServerResponse(addNewTableRequestMono.flatMap(request -> {
			final var tableMono = tableManagementService.addNewTable(request.toDomain());
			return tableMono.map(TableDTO::from);
		}));
	}

	public Mono<ServerResponse> addCustomer(ServerRequest serverRequest) {
		final var addCustomerToTableRequestDTO = serverRequest.bodyToMono(AddCustomerToTableRequestDTO.class);
		return toMonoServerResponse(addCustomerToTableRequestDTO.flatMap(request -> {
			final var tableMono = tableManagementService.addCustomerToTable(request.toDomain());
			return tableMono.map(TableDTO::from);
		}));
	}

	public Mono<ServerResponse> findByCustomer(ServerRequest request) {
		final var customerId = CustomerId.valueOf(request.pathVariable("customerId"));
		final var tableMono = tableRepository.findByCustomer(customerId);
		return toMonoServerResponse(tableMono);
	}

	public Mono<ServerResponse> findByName(ServerRequest request) {
		final var name = TableName.from(request.pathVariable("name"));
		final var tableMono = tableRepository.findByName(name);
		return toMonoServerResponse(tableMono);
	}

	public Mono<ServerResponse> getSector(ServerRequest request) {
		final var name = SectorName.valueOf(request.pathVariable("sectorName"));
		final Mono<Sector> sector = sectorRepository.findByName(name);
		return toMonoServerResponse(sector);
	}

	public Mono<ServerResponse> deleteById(ServerRequest request) {
		final var id = TableId.valueOf(request.pathVariable("tableId"));
		final var idMono = tableRepository.delete(id);
		return toMonoServerResponse(idMono);
	}

	public Mono<ServerResponse> generateTables(ServerRequest request) {
		final Flux<Table> tableFlux = generator.generateDefault();

		return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
				.body(tableFlux, Table.class);
	}

}
