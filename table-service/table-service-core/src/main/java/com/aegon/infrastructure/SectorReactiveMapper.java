package com.aegon.infrastructure;

import com.aegon.application.TableRepository;
import com.aegon.domain.MongoSectorDocument;
import com.aegon.domain.Sector;
import com.aegon.domain.SectorId;
import com.aegon.domain.SectorName;
import com.aegon.domain.TableId;
import com.aegon.util.lang.Mapper;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SectorReactiveMapper implements Mapper<Mono<MongoSectorDocument>, Mono<Sector>> {

	private final TableRepository tableRepository;

	@Override
	public Mono<Sector> map(Mono<MongoSectorDocument> documentMono) {
		return documentMono.flatMap(document -> Flux.fromIterable(document.getTableIds())
				.flatMap(idString -> tableRepository.findById(TableId.valueOf(idString)))
				.collectList()
				.map(tables -> Sector.builder()
						.id(SectorId.valueOf(document.getId()))
						.name(SectorName.valueOf(document.getName()))
						.maxTables(document.getMaxTables())
						.tables(new HashSet<>(tables))
						.build()));
	}
}
