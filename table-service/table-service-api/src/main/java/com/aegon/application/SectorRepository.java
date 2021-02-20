package com.aegon.application;

import com.aegon.domain.Sector;
import com.aegon.domain.SectorId;
import com.aegon.domain.SectorName;
import com.aegon.util.lang.DomainReactiveRepository;
import java.util.Collection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SectorRepository extends DomainReactiveRepository<SectorId, Sector> {

	Mono<Sector> findByName(SectorName name);

	Flux<Sector> saveAll(Collection<Sector> sectors);

}
