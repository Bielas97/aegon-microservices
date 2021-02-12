package com.aegon.domain;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;
import com.aegon.util.lang.exception.ExceptionErrors;

public class ApplicationUserPasswordException extends BaseException {

	private static final String PASSWORD_NOT_VALID_MSG = "Provided password is not valid";

	private ApplicationUserPasswordException(String msg) {
		super(new ExceptionError(msg));
	}

	private ApplicationUserPasswordException(ExceptionErrors errors) {
		super(errors);
	}

	public static ApplicationUserPasswordException notValid() {
		return new ApplicationUserPasswordException(PASSWORD_NOT_VALID_MSG);
	}

	public static ApplicationUserPasswordException of(ExceptionErrors errors) {
		return new ApplicationUserPasswordException(errors);
	}

	public static ApplicationUserPasswordException of(String msg) {
		return new ApplicationUserPasswordException(msg);
	}
}
