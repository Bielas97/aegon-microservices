package com.aegon;

import com.aegon.domain.Sector;
import com.aegon.requests.AddNewSectorRequest;
import com.aegon.requests.AddTableToSectorRequest;
import com.aegon.requests.AddTablesToSectorRequest;
import java.util.Collection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SectorManagementService {

	Mono<Sector> addNewSector(AddNewSectorRequest request);

	Flux<Sector> addNewSectors(Collection<AddNewSectorRequest> requests);

	Mono<Sector> addTableToSector(AddTableToSectorRequest request);

	Mono<Sector> addTablesToSector(AddTablesToSectorRequest request);

}
