package com.ankamagames.wakfu.common.game.world.havenWorld.building;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import gnu.trove.*;

public class BuildingMapSelector
{
    private static final Logger m_logger;
    private static final AbstractBuildingStruct[] EMPTY;
    private final TLongObjectHashMap<AbstractBuildingStruct> m_buildings;
    private AbstractBuildingStruct[] m_last;
    private short m_lastMapX;
    private short m_lastMapY;
    
    public BuildingMapSelector(final TLongObjectHashMap<AbstractBuildingStruct> buildings) {
        super();
        this.m_last = BuildingMapSelector.EMPTY;
        this.m_lastMapX = 32767;
        this.m_lastMapY = 32767;
        this.m_buildings = buildings;
    }
    
    public AbstractBuildingStruct[] getBuildingsInMap(final short mapCoordX, final short mapCoordY) {
        if (this.m_lastMapX == mapCoordX && this.m_lastMapY == mapCoordY) {
            return this.m_last;
        }
        final int cellX = mapCoordX * 18;
        final int cellY = mapCoordY * 18;
        final Rect bounds = new Rect(cellX, cellX + 18 - 1, cellY, cellY + 18 - 1);
        final ArrayList<AbstractBuildingStruct> result = new ArrayList<AbstractBuildingStruct>();
        this.m_buildings.forEachValue((TObjectProcedure<AbstractBuildingStruct>)new TObjectProcedure<AbstractBuildingStruct>() {
            @Override
            public boolean execute(final AbstractBuildingStruct b) {
                if (bounds.containsOrIntersect(b.getCellBounds())) {
                    result.add(b);
                }
                return true;
            }
        });
        final AbstractBuildingStruct[] buildings = result.isEmpty() ? BuildingMapSelector.EMPTY : result.toArray(new AbstractBuildingStruct[result.size()]);
        this.m_lastMapX = mapCoordX;
        this.m_lastMapY = mapCoordY;
        return this.m_last = buildings;
    }
    
    public void cleanCache() {
        this.m_last = BuildingMapSelector.EMPTY;
        this.m_lastMapX = 32767;
        this.m_lastMapY = 32767;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BuildingMapSelector.class);
        EMPTY = new AbstractBuildingStruct[0];
    }
}
