package com.aegon.application;

import com.aegon.SectorRepository;
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
		final var sectorId = request.getSectorId();
		sectorRepository.findById(sectorId)
				.flatMap(sector -> {
					if (sector.getTables().size() >= sector.getMaxTables()) {
						return Mono.error(TooManyTablesInSectorException.tooMany());
					}
					return Mono.empty();
				});
	}
}
