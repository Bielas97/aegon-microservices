package com.aegon.domain;

import com.aegon.util.lang.SimpleId;

public class ApplicationUserPassword extends SimpleId<String> {

	private ApplicationUserPassword(String internal) {
		super(internal);
	}

	public static ApplicationUserPassword valueOf(String password) {
		return new ApplicationUserPassword(password);
	}

	public boolean sameAs(ApplicationUserPassword that) {
		return this.equals(that);
	}

}
