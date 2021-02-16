package com.aegon.infrastructure;

import com.aegon.SectorRepository;
import com.aegon.TableRepository;
import com.aegon.domain.MongoSectorDocument;
import com.aegon.domain.Sector;
import com.aegon.domain.SectorId;
import com.aegon.domain.SectorName;
import com.aegon.util.lang.SimpleId;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SectorRepositoryImpl implements SectorRepository {

	private final MongoSectorRepository mongoSectorRepository;

	private final TableRepository tableRepository;

	private final SectorReactiveMapper mapper;

	@Override
	public Mono<Sector> findByName(SectorName name) {
		final var sectorMono = mongoSectorRepository.findByName(name.getInternal());
		return mapper.map(sectorMono)
				.switchIfEmpty(Mono.error(SectorRepositoryException.of("Sector not found")));
	}

	@Override
	public Mono<Sector> findById(SectorId id) {
		final var sectorMono = mongoSectorRepository.findById(id.getInternal());
		return mapper.map(sectorMono)
				.switchIfEmpty(Mono.error(SectorRepositoryException.of("Sector not found")));
	}

	@Override
	public Mono<Sector> save(Sector sector) {
		final MongoSectorDocument document = MongoSectorDocument.builder()
				.name(sector.getName().getInternal())
				.maxTables(sector.getMaxTables())
				.tableIds(sector.getTableIds().stream().map(SimpleId::getInternal).collect(Collectors.toSet()))
				.build();
		final var saved = mongoSectorRepository.save(document);
		return mapper.map(saved);
	}

	@Override
	public Mono<SectorId> delete(SectorId id) {
		return mongoSectorRepository.deleteById(id.getInternal())
				.flatMap(unused -> Mono.just(id));
	}

	@Override
	public Mono<Sector> update(Sector sector) {
		return mongoSectorRepository.findById(sector.getId().getInternal())
				.flatMap(document -> {
					document.update(sector);
					final var updated = mongoSectorRepository.save(document);
					return mapper.map(updated);
				});
	}
}
