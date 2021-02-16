package com.aegon.infrastructure;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class TableRepositoryException extends BaseException {

	private TableRepositoryException(String msg) {
		super(new ExceptionError(msg));
	}

	public static TableRepositoryException of(String msg) {
		return new TableRepositoryException(msg);
	}

}
