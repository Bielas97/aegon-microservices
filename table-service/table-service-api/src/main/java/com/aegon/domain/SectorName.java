package com.aegon.domain;

import com.aegon.util.lang.SimpleId;

public class SectorName extends SimpleId<String> {

	private SectorName(String internal) {
		super(internal);
	}

	public static SectorName valueOf(String name) {
		return new SectorName(name);
	}
}
