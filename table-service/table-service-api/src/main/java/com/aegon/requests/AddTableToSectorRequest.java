package com.aegon.requests;

import com.aegon.domain.SectorId;
import com.aegon.domain.TableId;

public record AddTableToSectorRequest(SectorId sectorId, TableId tableId) {

}
