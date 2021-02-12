package com.aegon.proxy;

import com.aegon.util.lang.SimpleId;

public class CustomerId extends SimpleId<String> {

	private CustomerId(String internal) {
		super(internal);
	}

	public static CustomerId valueOf(String id) {
		return new CustomerId(id);
	}
}
