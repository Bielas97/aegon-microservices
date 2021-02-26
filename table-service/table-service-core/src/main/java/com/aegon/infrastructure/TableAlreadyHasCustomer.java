package com.aegon.infrastructure;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class TableAlreadyHasCustomer extends BaseException {

	private static final String THE_TABLE_ALREADY_HAS_CUSTOMER_WITH_THIS_ID = "The table already has customer with this id";

	private static final TableAlreadyHasCustomer DEFAULT = new TableAlreadyHasCustomer(THE_TABLE_ALREADY_HAS_CUSTOMER_WITH_THIS_ID);

	private TableAlreadyHasCustomer(String msg) {
		super(new ExceptionError(msg));
	}

	public static TableAlreadyHasCustomer err() {
		return DEFAULT;
	}

}
