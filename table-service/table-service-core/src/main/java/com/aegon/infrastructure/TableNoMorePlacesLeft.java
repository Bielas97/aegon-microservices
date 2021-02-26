package com.aegon.infrastructure;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class TableNoMorePlacesLeft extends BaseException {

	private static final String THIS_TABLE_DOES_NOT_HAVE_ANY_MORE_PLACES_LEFT = "This table does not have any more places left";

	private static final TableNoMorePlacesLeft DEFAULT = new TableNoMorePlacesLeft(THIS_TABLE_DOES_NOT_HAVE_ANY_MORE_PLACES_LEFT);

	private TableNoMorePlacesLeft(String msg) {
		super(new ExceptionError(msg));
	}

	public static TableNoMorePlacesLeft err() {
		return DEFAULT;
	}

}
