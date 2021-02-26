package com.aegon.infrastructure;

import com.aegon.SectorRepository;
import com.aegon.TableRepository;
import com.aegon.domain.MongoSectorDocument;
import com.aegon.domain.Sector;
import com.aegon.domain.SectorId;
import com.aegon.domain.SectorName;
import com.aegon.util.lang.SimpleId;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
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
				.switchIfEmpty(Mono.error(SectorRepositoryException.of(String.format("Sector with name %s not found", name.getInternal()))));
	}

	@Override
	public Flux<Sector> saveAll(Collection<Sector> sectors) {
		final Set<MongoSectorDocument> documents = sectors.stream().map(sector -> MongoSectorDocument.builder()
				.name(sector.getName().getInternal())
				.maxTables(sector.getMaxTables())
				.tableIds(sector.getTableIds().stream().map(SimpleId::getInternal).collect(Collectors.toSet()))
				.build())
				.collect(Collectors.toSet());
		final Flux<MongoSectorDocument> mongoSectorDocumentFlux = mongoSectorRepository.saveAll(documents);
		return mongoSectorDocumentFlux.flatMap(document -> {
			final Mono<MongoSectorDocument> documentMono = Mono.just(document);
			return mapper.map(documentMono);
		})
				.switchIfEmpty(Mono.error(SectorRepositoryException.of(String.format("Sectors with ids %s could not be saved",
						documents.stream().map(MongoSectorDocument::getId).collect(Collectors.toList())))));
	}

	@Override
	public Mono<SectorName> deleteByName(SectorName name) {
		return mongoSectorRepository.deleteByName(name.getInternal())
				.map(unused -> name)
				.switchIfEmpty(Mono.error(SectorRepositoryException.of(String.format("Sector with name %s not found", name.getInternal()))));
	}

	@Override
	public Mono<Sector> findById(SectorId id) {
		final var sectorMono = mongoSectorRepository.findById(id.getInternal());
		return mapper.map(sectorMono)
				.switchIfEmpty(Mono.error(SectorRepositoryException.of(String.format("Sector with id %s not found", id.getInternal()))));
	}

	@Override
	public Mono<Sector> save(Sector sector) {
		final MongoSectorDocument document = MongoSectorDocument.builder()
				.name(sector.getName().getInternal())
				.maxTables(sector.getMaxTables())
				.tableIds(sector.getTableIds().stream().map(SimpleId::getInternal).collect(Collectors.toSet()))
				.build();
		final var saved = mongoSectorRepository.save(document)
				.switchIfEmpty(Mono.error(SectorRepositoryException.of(String.format("Sector with name %s coudl not be saved", sector.getName().getInternal()))));
		return mapper.map(saved);
	}

	@Override
	public Mono<SectorId> delete(SectorId id) {
		return mongoSectorRepository.findById(id.getInternal())
				.flatMap(document -> mongoSectorRepository.delete(document).then(Mono.just(SectorId.valueOf(document.getId()))));
	}

	@Override
	public Mono<Sector> update(Sector sector) {
		return mongoSectorRepository.findById(sector.getId().getInternal())
				.flatMap(document -> {
					document.update(sector);
					final var updated = mongoSectorRepository.save(document);
					return mapper.map(updated);
				})
				.switchIfEmpty(Mono.error(SectorRepositoryException.of(String.format("Sector with id %s could not be updated", sector.getId().getInternal()))));
	}
}
