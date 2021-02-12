package com.aegon;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class ApplicationUserServiceException extends BaseException {

	private ApplicationUserServiceException(String msg) {
		super(new ExceptionError(msg));
	}

	public static ApplicationUserServiceException of(String msg) {
		return new ApplicationUserServiceException(msg);
	}

}
