package com.aegon.application.generator;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

class TableGeneratorException extends BaseException {

	private TableGeneratorException(String msg) {
		super(new ExceptionError(msg));
	}

	public static TableGeneratorException of(String msg) {
		return new TableGeneratorException(msg);
	}

}
