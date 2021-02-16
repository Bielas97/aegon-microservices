package com.aegon.infrastructure;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class SectorRepositoryException extends BaseException {

	private SectorRepositoryException(String msg) {
		super(new ExceptionError(msg));
	}

	public static SectorRepositoryException of(String msg) {
		return new SectorRepositoryException(msg);
	}

}
