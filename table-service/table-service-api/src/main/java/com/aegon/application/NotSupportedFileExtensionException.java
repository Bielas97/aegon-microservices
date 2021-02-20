package com.aegon.application;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class NotSupportedFileExtensionException extends BaseException {

	private static final String DEFAULT_MSG = "This files are not supported as import generating files";

	private NotSupportedFileExtensionException(String msg) {
		super(new ExceptionError(msg));
	}

	public static NotSupportedFileExtensionException getInstance() {
		return new NotSupportedFileExtensionException(DEFAULT_MSG);
	}

}
