package com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;

public class MissingBuilding extends ConstructionError
{
    private static final Logger m_logger;
    private final int m_buildingType;
    private final int m_quantity;
    
    public MissingBuilding(final ModificationItem item, final int quantity, final int buildingType) {
        super(item);
        this.m_quantity = quantity;
        this.m_buildingType = buildingType;
    }
    
    public int getBuildingType() {
        return this.m_buildingType;
    }
    
    public int getQuantity() {
        return this.m_quantity;
    }
    
    @Override
    public final Type getType() {
        return Type.MissingBuilding;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MissingBuilding.class);
    }
}
