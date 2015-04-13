package com.ankamagames.wakfu.common.game.havenWorld.procedure;

import com.ankamagames.wakfu.common.game.world.havenWorld.validator.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;

public class MissingBuildingCountComputer2
{
    private final HavenWorldDataProvider m_world;
    private final short m_definitionId;
    private TIntIntHashMap m_missingBuildings;
    
    public MissingBuildingCountComputer2(final HavenWorldDataProvider world, final short definitionId) {
        super();
        this.m_missingBuildings = new TIntIntHashMap();
        this.m_world = world;
        this.m_definitionId = definitionId;
        this.compute();
    }
    
    private void compute() {
        final AbstractBuildingDefinition definition = HavenWorldDefinitionManager.INSTANCE.getBuilding(this.m_definitionId);
        final BuildingCatalogEntry catalogEntry = HavenWorldDefinitionManager.INSTANCE.getBuildingCatalogEntry(definition.getCatalogEntryId());
        catalogEntry.forEachCondition(new TObjectProcedure<BuildingCondition>() {
            @Override
            public boolean execute(final BuildingCondition object) {
                final int missingQuantity = MissingBuildingCountComputer2.this.getMissingQuantity(object);
                if (missingQuantity > 0) {
                    MissingBuildingCountComputer2.this.m_missingBuildings.adjustOrPutValue(object.getBuildingTypeNeeded(), missingQuantity, missingQuantity);
                }
                return true;
            }
        });
    }
    
    private int getMissingQuantity(final BuildingCondition condition) {
        final BuildingOfTypeCount2 computer = new BuildingOfTypeCount2(condition.getBuildingTypeNeeded());
        this.m_world.forEachBuilding(computer);
        return MathHelper.max(0, condition.getQuantity() - computer.getCount(), new int[0]);
    }
    
    public boolean forEachMissingEntry(final TIntIntProcedure procedure) {
        return this.m_missingBuildings.forEachEntry(procedure);
    }
    
    public boolean hasSomeMissingConditions() {
        return !this.m_missingBuildings.isEmpty();
    }
}
