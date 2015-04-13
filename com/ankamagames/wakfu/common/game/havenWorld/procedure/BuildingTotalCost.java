package com.ankamagames.wakfu.common.game.havenWorld.procedure;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;

public class BuildingTotalCost implements TObjectProcedure<AbstractBuildingDefinition>
{
    private final short m_buildingTo;
    private long m_kamasTotalCost;
    private long m_resourcesTotalCost;
    
    public BuildingTotalCost(final short referenceBuildingId) {
        super();
        this.m_buildingTo = referenceBuildingId;
    }
    
    @Override
    public boolean execute(final AbstractBuildingDefinition building) {
        if (this.isBuildingFrom(building.getId()) || this.m_buildingTo == building.getId()) {
            this.m_kamasTotalCost += building.getKamasCost();
            this.m_resourcesTotalCost += building.getResourcesCost();
        }
        return true;
    }
    
    private boolean isBuildingFrom(final short buildingRefId) {
        for (BuildingEvolution evolution = HavenWorldDefinitionManager.INSTANCE.getEvolutionFromBuilding(buildingRefId); evolution != null; evolution = HavenWorldDefinitionManager.INSTANCE.getEvolutionFromBuilding(evolution.getBuildingToId())) {
            if (evolution.getBuildingToId() == this.m_buildingTo) {
                return true;
            }
        }
        return false;
    }
    
    public long getKamasTotalCost() {
        return this.m_kamasTotalCost;
    }
    
    public long getResourcesTotalCost() {
        return this.m_resourcesTotalCost;
    }
}
