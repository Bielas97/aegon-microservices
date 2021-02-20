package com.aegon.application;

import com.aegon.infrastructure.SectorRepositoryException;
import com.aegon.requests.AddNewTableRequest;
import com.aegon.util.lang.Preconditions;
import reactor.core.publisher.Mono;

public class AddNewTableValidator implements TableValidator {

	private final AddNewTableRequest request;

	private final SectorRepository sectorRepository;

	public AddNewTableValidator(AddNewTableRequest request, SectorRepository sectorRepository) {
		this.request = Preconditions.requireNonNull(request);
		this.sectorRepository = Preconditions.requireNonNull(sectorRepository);
	}

	@Override
	public void validate() {
		//TODO test it
		final var sectorName = request.getName().getSectorName();
		sectorRepository.findByName(sectorName)
				.flatMap(sector -> {
					if (sector.getTablesAmount() >= sector.getMaxTables()) {
						return Mono.error(TooManyTablesInSectorException.tooMany());
					}
					return Mono.empty();
				})
				.switchIfEmpty(Mono.error(SectorRepositoryException.of("Sector not found")));
	}
}
