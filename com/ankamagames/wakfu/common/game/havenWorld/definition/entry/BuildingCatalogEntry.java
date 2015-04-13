package com.ankamagames.wakfu.common.game.havenWorld.definition.entry;

import java.util.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import gnu.trove.*;

public class BuildingCatalogEntry extends HavenWorldCatalogEntry
{
    private final ArrayList<BuildingCondition> m_conditions;
    private final boolean m_buyable;
    private final int m_order;
    private final int m_buildingType;
    
    public BuildingCatalogEntry(final short id, final int categoryId, final int order, final int buildingType, final boolean buyable, final short maxQuantity) {
        super(id, categoryId, maxQuantity);
        this.m_conditions = new ArrayList<BuildingCondition>();
        this.m_order = order;
        this.m_buildingType = buildingType;
        this.m_buyable = buyable;
    }
    
    @Override
    public int getKamaCost() {
        return BuildingDefinitionHelper.getFirstBuildingFor(this).getKamasCost();
    }
    
    public boolean isDecoOnly() {
        return BuildingDefinitionHelper.getFirstBuildingFor(this).isDecoOnly();
    }
    
    public int getResourceCost() {
        return BuildingDefinitionHelper.getFirstBuildingFor(this).getResourcesCost();
    }
    
    public int getNeededWorkers() {
        return BuildingDefinitionHelper.getFirstBuildingFor(this).getNeededWorkers();
    }
    
    public int getGrantedWorkers() {
        return BuildingDefinitionHelper.getFirstBuildingFor(this).getGrantedWorkers();
    }
    
    public boolean isBuyable() {
        return this.m_buyable;
    }
    
    public void addCondition(final int buildingTypeNeeded, final int quantity) {
        this.m_conditions.add(new BuildingCondition(buildingTypeNeeded, quantity));
    }
    
    public boolean forEachCondition(final TObjectProcedure<BuildingCondition> procedure) {
        for (int i = 0; i < this.m_conditions.size(); ++i) {
            if (!procedure.execute(this.m_conditions.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        return super.toString() + ", m_conditions=" + this.m_conditions + ", m_buyable=" + this.m_buyable + '}';
    }
    
    public int getOrder() {
        return this.m_order;
    }
    
    public int getBuildingType() {
        return this.m_buildingType;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BuildingCatalogEntry)) {
            return false;
        }
        final BuildingCatalogEntry that = (BuildingCatalogEntry)o;
        return this.getId() == that.getId();
    }
    
    @Override
    public int hashCode() {
        return this.getId();
    }
}
