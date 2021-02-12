package com.aegon.domain;

import com.aegon.util.lang.SimpleId;

public class TableId extends SimpleId<String> {

	private TableId(String internal) {
		super(internal);
	}

	public static TableId valueOf(String id) {
		return new TableId(id);
	}
}
