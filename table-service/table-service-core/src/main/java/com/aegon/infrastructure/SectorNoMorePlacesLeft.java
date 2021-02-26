package com.aegon.infrastructure;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class SectorNoMorePlacesLeft extends BaseException {

	private static final String THIS_SECTOR_DOES_NOT_HAVE_ANY_MORE_PLACES_LEFT = "This sector does not have any more places left";

	private static final SectorNoMorePlacesLeft DEFAULT = new SectorNoMorePlacesLeft(THIS_SECTOR_DOES_NOT_HAVE_ANY_MORE_PLACES_LEFT);

	private SectorNoMorePlacesLeft(String msg) {
		super(new ExceptionError(msg));
	}

	public static SectorNoMorePlacesLeft err() {
		return DEFAULT;
	}

}
