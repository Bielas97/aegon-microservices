package com.aegon.domain;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class TableFreePlacesException extends BaseException {

	private static final String TABLE_HAS_MORE_OCCUPIED_PLACES_THAN_IT_IS_POSSIBLE = "Table has more occupied places than it is possible";

	private static final TableFreePlacesException DEFAULT = new TableFreePlacesException(TABLE_HAS_MORE_OCCUPIED_PLACES_THAN_IT_IS_POSSIBLE);

	private TableFreePlacesException(String msg){
		super(new ExceptionError(msg));
	}

	public static TableFreePlacesException err(){
		return DEFAULT;
	}

}
