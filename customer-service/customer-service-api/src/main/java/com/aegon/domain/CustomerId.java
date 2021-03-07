package com.aegon.domain;

import com.aegon.util.lang.SimpleId;

public class CustomerId extends SimpleId<String> {

	private CustomerId(String internal) {
		super(internal);
	}

	public static CustomerId valueOf(String internal) {
		return new CustomerId(internal);
	}
}
