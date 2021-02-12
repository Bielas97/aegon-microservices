package com.aegon.domain;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class UsernameException extends BaseException {

	private static final String USERNAME_NOT_VALID_MSG = "Provided username is not valid";

	private UsernameException(String msg) {
		super(new ExceptionError(msg));
	}

	public static UsernameException notValid() {
		return new UsernameException(USERNAME_NOT_VALID_MSG);
	}
}
