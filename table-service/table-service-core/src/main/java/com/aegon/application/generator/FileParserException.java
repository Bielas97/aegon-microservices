package com.aegon.application.generator;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

class FileParserException extends BaseException {

	private FileParserException(String msg) {
		super(new ExceptionError(msg));
	}

	public static FileParserException of(String msg) {
		return new FileParserException(msg);
	}

}
