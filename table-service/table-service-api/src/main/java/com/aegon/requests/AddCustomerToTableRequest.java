package com.aegon.requests;

import com.aegon.domain.TableId;
import com.aegon.proxy.CustomerId;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddCustomerToTableRequest {

	private final TableId tableId;

	private final CustomerId customerId;

}
