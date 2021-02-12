package com.aegon.domain;

import com.aegon.proxy.CustomerId;
import com.aegon.requests.AddCustomerToTableRequest;

public class AddCustomerToTableRequestDTO {

	public String tableId;

	public String customerId;

	public AddCustomerToTableRequest toDomain() {
		return AddCustomerToTableRequest.builder()
				.tableId(TableId.valueOf(tableId))
				.customerId(CustomerId.valueOf(customerId))
				.build();
	}

}
