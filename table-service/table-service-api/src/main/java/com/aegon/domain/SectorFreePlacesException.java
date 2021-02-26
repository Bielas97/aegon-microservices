package com.aegon.domain;

import com.aegon.util.lang.exception.BaseException;
import com.aegon.util.lang.exception.ExceptionError;

public class SectorFreePlacesException extends BaseException {

	private static final String SECTOR_HAS_MORE_OCCUPIED_PLACES_THAN_IT_IS_POSSIBLE = "Sector has more occupied places than it is possible";

	private static final SectorFreePlacesException DEFAULT = new SectorFreePlacesException(SECTOR_HAS_MORE_OCCUPIED_PLACES_THAN_IT_IS_POSSIBLE);

	private SectorFreePlacesException(String msg){
		super(new ExceptionError(msg));
	}

	public static SectorFreePlacesException err(){
		return DEFAULT;
	}

}
