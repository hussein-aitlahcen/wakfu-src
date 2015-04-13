package com.ankamagames.wakfu.common.game.havenWorld.procedure;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;

public class BuildingOfTypeCount implements TObjectProcedure<Building>
{
    private final int m_type;
    private int m_count;
    
    public BuildingOfTypeCount(final int type) {
        super();
        this.m_type = type;
        this.m_count = 0;
    }
    
    @Override
    public boolean execute(final Building building) {
        final BuildingCatalogEntry catalogEntry = HavenWorldDefinitionManager.INSTANCE.getBuildingCatalogEntry(building.getDefinition().getCatalogEntryId());
        if (BuildingDefinitionHelper.getState(building.getDefinition()) != BuildingDefinitionHelper.ConstructionState.DONE) {
            return true;
        }
        final int type = catalogEntry.getBuildingType();
        if (HavenWorldConstants.isBuildingTypeLinked(type, this.m_type) || type == this.m_type) {
            ++this.m_count;
        }
        return true;
    }
    
    public int getCount() {
        return this.m_count;
    }
}
