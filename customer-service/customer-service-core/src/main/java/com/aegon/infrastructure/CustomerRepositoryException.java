package com.aegon.infrastructure;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class CustomerRepositoryException extends BaseException {

	private CustomerRepositoryException(String msg) {
		super(new ExceptionError(msg));
	}

	public static CustomerRepositoryException of(String msg) {
		return new CustomerRepositoryException(msg);
	}

}
