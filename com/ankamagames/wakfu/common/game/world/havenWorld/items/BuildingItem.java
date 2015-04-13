package com.ankamagames.wakfu.common.game.world.havenWorld.items;

import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;

public class BuildingItem implements ModificationItem<BuildingCatalogEntry>
{
    public static final BuildingItem[] EMPTY;
    private final AbstractBuildingStruct m_buildingInfo;
    
    public BuildingItem(final AbstractBuildingStruct building) {
        super();
        this.m_buildingInfo = building;
    }
    
    @Override
    public ItemLayer getLayer() {
        return ItemLayer.BUILDING;
    }
    
    @Override
    public Point2i getCell() {
        return new Point2i(this.m_buildingInfo.getCellX(), this.m_buildingInfo.getCellY());
    }
    
    @Override
    public BuildingCatalogEntry getCatalogEntry() {
        return this.m_buildingInfo.getCatalogEntry();
    }
    
    @Override
    public boolean equals(final Object o) {
        return Helper.equals(this, o);
    }
    
    @Override
    public int hashCode() {
        return Helper.hashCode(this);
    }
    
    @Override
    public long getUid() {
        return this.m_buildingInfo.getBuildingUid();
    }
    
    public AbstractBuildingStruct getBuildingInfo() {
        return this.m_buildingInfo;
    }
    
    static {
        EMPTY = new BuildingItem[0];
    }
}
