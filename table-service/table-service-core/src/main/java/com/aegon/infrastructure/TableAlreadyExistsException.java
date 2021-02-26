package com.aegon.infrastructure;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class TableAlreadyExistsException extends BaseException {

	public static final String TABLE_WITH_THIS_PARAMETERS_ALREADY_EXISTS = "Table with this parameters already exists";

	public static final TableAlreadyExistsException DEFAULT = new TableAlreadyExistsException(TABLE_WITH_THIS_PARAMETERS_ALREADY_EXISTS);

	public TableAlreadyExistsException(String msg) {
		super(new ExceptionError(msg));
	}

	public static TableAlreadyExistsException error() {
		return DEFAULT;
	}

}
