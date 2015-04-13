package com.ankamagames.wakfu.common.game.havenWorld.definition;

import org.apache.log4j.*;

public class BuildingCondition
{
    private static final Logger m_logger;
    private final int m_buildingTypeNeeded;
    private final int m_quantity;
    
    public BuildingCondition(final int buildingTypeNeeded, final int quantity) {
        super();
        this.m_buildingTypeNeeded = buildingTypeNeeded;
        this.m_quantity = quantity;
    }
    
    public int getBuildingTypeNeeded() {
        return this.m_buildingTypeNeeded;
    }
    
    public int getQuantity() {
        return this.m_quantity;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BuildingCondition.class);
    }
}
