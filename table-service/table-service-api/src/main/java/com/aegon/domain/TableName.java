package com.aegon.domain;

import com.aegon.util.lang.SimpleId;

public class TableName extends SimpleId<String> {

	private TableName(String internal) {
		super(internal);
	}

	public static TableName valueOf(String name) {
		return new TableName(name);
	}
}
