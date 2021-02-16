package com.aegon.application;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class TooManyTablesInSectorException extends BaseException {

	private static final String TOO_MANY_TABLES_MSG = "This sector cannot have more tables";

	private TooManyTablesInSectorException(String err) {
		super(new ExceptionError(err));
	}

	public static TooManyTablesInSectorException tooMany() {
		return new TooManyTablesInSectorException(TOO_MANY_TABLES_MSG);
	}

}
