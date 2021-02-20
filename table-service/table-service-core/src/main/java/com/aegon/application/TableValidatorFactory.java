package com.aegon.application;

import com.aegon.requests.AddNewTableRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TableValidatorFactory {

	private final SectorRepository sectorRepository;

	public TableValidator of(AddNewTableRequest request) {
		return new AddNewTableValidator(request, sectorRepository);
	}

}
