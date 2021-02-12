package com.aegon.application;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class ChangePasswordException extends BaseException {

	private ChangePasswordException(String msg) {
		super(new ExceptionError(msg));
	}

	public static ChangePasswordException of(String msg) {
		return new ChangePasswordException(msg);
	}

}
