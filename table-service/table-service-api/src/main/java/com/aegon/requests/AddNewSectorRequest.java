package com.aegon.requests;

import com.aegon.domain.SectorName;

public record AddNewSectorRequest(SectorName name, Integer maxTables) {

}
