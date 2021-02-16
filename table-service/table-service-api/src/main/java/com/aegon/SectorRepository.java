package com.aegon;

import com.aegon.domain.Sector;
import com.aegon.domain.SectorId;
import com.aegon.domain.SectorName;
import com.aegon.util.lang.DomainReactiveRepository;
import reactor.core.publisher.Mono;

public interface SectorRepository extends DomainReactiveRepository<SectorId, Sector> {

	Mono<Sector> findByName(SectorName name);

}
