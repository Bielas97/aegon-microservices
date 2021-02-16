package com.aegon.requests;

import com.aegon.domain.SectorId;
import com.aegon.domain.TableId;
import java.util.Set;

public record AddTablesToSectorRequest(SectorId sectorId, Set<TableId> tables) {

}
