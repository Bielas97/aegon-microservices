package com.aegon;

import com.aegon.domain.Sector;
import com.aegon.domain.SectorId;
import com.aegon.domain.SectorName;
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

	Mono<SectorId> delete(SectorId sectorId);

	Mono<SectorId> deleteByName(SectorName name);

}
