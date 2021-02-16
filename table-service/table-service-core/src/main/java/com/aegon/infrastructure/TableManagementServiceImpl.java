package com.aegon.infrastructure;

import com.aegon.SectorRepository;
import com.aegon.TableManagementService;
import com.aegon.TableRepository;
import com.aegon.application.TableValidatorFactory;
import com.aegon.domain.Table;
import com.aegon.requests.AddCustomerToTableRequest;
import com.aegon.requests.AddNewTableRequest;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TableManagementServiceImpl implements TableManagementService {

	private final SectorRepository sectorRepository;

	private final TableRepository tableRepository;

	private final TableValidatorFactory validatorFactory;

	@Override
	public Mono<Table> addNewTable(AddNewTableRequest request) {
		validatorFactory.of(request).validate();
		return sectorRepository.findByName(request.getSectorName())
				.flatMap(sector -> {
					final var table = Table.builder()
							.name(request.getName())
							.customers(Collections.emptySet())
							.maxPlaces(request.getMaxPlaces())
							.sectorId(sector.getId())
							.build();
					return tableRepository.save(table);
				});
	}

	@Override
	public Mono<Table> addCustomerToTable(AddCustomerToTableRequest request) {
		return tableRepository.findById(request.getTableId())
				.flatMap(table -> {
					table.addCustomer(request.getCustomerId());
					return tableRepository.update(table);
				});
	}
}
