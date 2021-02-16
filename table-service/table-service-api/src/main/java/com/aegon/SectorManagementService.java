package com.aegon;

import com.aegon.domain.Sector;
import com.aegon.requests.AddNewSectorRequest;
import com.aegon.requests.AddTableToSectorRequest;
import com.aegon.requests.AddTablesToSectorRequest;
import reactor.core.publisher.Mono;

public interface SectorManagementService {

	Mono<Sector> addNewSector(AddNewSectorRequest request);

	Mono<Sector> addTableToSector(AddTableToSectorRequest request);

	Mono<Sector> addTablesToSector(AddTablesToSectorRequest request);

}
