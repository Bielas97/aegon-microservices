package com.aegon.application;

import com.aegon.TableManagementService;
import com.aegon.TableRepository;
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

	private final TableRepository repository;

	@Override
	public Mono<Table> addNewTable(AddNewTableRequest request) {
		final var table = Table.builder()
				.name(request.getName())
				.customers(Collections.emptySet())
				.maxPlaces(request.getMaxPlaces())
				.build();
		return repository.save(table);
	}

	@Override
	public Mono<Table> addCustomerToTable(AddCustomerToTableRequest request) {
		return repository.findById(request.getTableId())
				.flatMap(table -> {
					table.addCustomer(request.getCustomerId());
					return repository.update(table);
				});
	}
}
