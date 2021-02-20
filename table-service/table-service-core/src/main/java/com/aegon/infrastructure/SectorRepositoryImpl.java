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
				.switchIfEmpty(Mono.error(SectorRepositoryException.of("Sector not found")));
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
		});
	}

	@Override
	public Mono<Sector> findById(SectorId id) {
		final var sectorMono = mongoSectorRepository.findById(id.getInternal());
		return mapper.map(sectorMono)
				.onErrorResume(e -> {
					e.printStackTrace();
					throw new RuntimeException(e);
				})
				.onErrorContinue((e, d) -> {
					e.printStackTrace();
					d.toString();
				})
				.doOnError(e -> {
					e.printStackTrace();
				});
		//				.switchIfEmpty(Mono.error(SectorRepositoryException.of("Sector not found")));
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
					final var updated = mongoSectorRepository.save(document)
							.switchIfEmpty(Mono.defer(() -> {
								throw new RuntimeException("mono empty");
							}));
					return mapper.map(updated);
				})
				.onErrorResume(e -> {
					e.printStackTrace();
					throw new RuntimeException(e);
				});
	}
}
