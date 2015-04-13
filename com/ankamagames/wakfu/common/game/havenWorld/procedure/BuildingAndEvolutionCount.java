package com.ankamagames.wakfu.common.game.havenWorld.procedure;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;

public class BuildingAndEvolutionCount implements TObjectProcedure<Building>
{
    private final short m_lastEvolution;
    private int m_count;
    
    public BuildingAndEvolutionCount(final short referenceBuildingId) {
        super();
        this.m_lastEvolution = this.getLastEvolution(referenceBuildingId);
    }
    
    @Override
    public boolean execute(final Building building) {
        if (this.m_lastEvolution == this.getLastEvolution(building.getDefinition().getId())) {
            ++this.m_count;
        }
        return true;
    }
    
    private short getLastEvolution(final short buildingRefId) {
        short lastBuildingEvolution = buildingRefId;
        for (BuildingEvolution evolution = HavenWorldDefinitionManager.INSTANCE.getEvolutionFromBuilding(buildingRefId); evolution != null; evolution = HavenWorldDefinitionManager.INSTANCE.getEvolutionFromBuilding(evolution.getBuildingToId())) {
            lastBuildingEvolution = evolution.getBuildingToId();
        }
        return lastBuildingEvolution;
    }
    
    public int getCount() {
        return this.m_count;
    }
}
