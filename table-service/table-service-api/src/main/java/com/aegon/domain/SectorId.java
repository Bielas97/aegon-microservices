package com.aegon.domain;

import com.aegon.util.lang.SimpleId;

public class SectorId extends SimpleId<String> {

	private SectorId(String internal) {
		super(internal);
	}

	public static SectorId valueOf(String id) {
		return new SectorId(id);
	}
}
