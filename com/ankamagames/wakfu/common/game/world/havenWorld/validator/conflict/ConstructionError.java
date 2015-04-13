package com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict;

import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class ConstructionError
{
    protected final ModificationItem m_item;
    
    protected ConstructionError(final ModificationItem item) {
        super();
        this.m_item = item;
    }
    
    public ModificationItem getItem() {
        return this.m_item;
    }
    
    public Point2i getCell() {
        return this.m_item.getCell();
    }
    
    public abstract Type getType();
    
    public boolean equals(final ConstructionError error) {
        return this == error || (error != null && this.getClass() == error.getClass() && this.m_item.equals(error.m_item));
    }
    
    public enum Type
    {
        Conflict, 
        MissingKama, 
        MissingWealth, 
        MissingWorker, 
        BuildingDependency, 
        MissingBuilding, 
        MissingResources, 
        BuildingMaxQuantityReached;
    }
}
