package com.ankamagames.wakfu.common.game.havenWorld;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.building.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public abstract class AbstractBuildingStruct<T extends AbstractEditorGroupMap>
{
    private static final Logger m_logger;
    private final long m_buildingUid;
    private final int m_buildingDefinitionId;
    private final short m_cellX;
    private final short m_cellY;
    private final short m_cellZ = 0;
    private int m_itemId;
    private final AbstractEditorGroupMapLibrary<T> m_library;
    private T m_map;
    
    protected AbstractBuildingStruct(final AbstractEditorGroupMapLibrary<T> library, final long buildingUid, final int buildingDefinitionId, final int itemId, final short cellX, final short cellY) {
        super();
        this.m_library = library;
        this.m_buildingUid = buildingUid;
        this.m_buildingDefinitionId = buildingDefinitionId;
        this.m_cellX = cellX;
        this.m_cellY = cellY;
        this.setItemId(itemId);
    }
    
    protected AbstractBuildingStruct(final AbstractEditorGroupMapLibrary<T> library, final RawHavenWorldBuildings.Building b) {
        this(library, b.building.uid, b.building.id, b.building.equippedSkinItemId, b.building.x, b.building.y);
    }
    
    protected AbstractBuildingStruct(final AbstractEditorGroupMapLibrary<T> library, final Building b) {
        this(library, b.getUid(), b.getDefinition().getId(), b.getEquippedItemId(), b.getX(), b.getY());
    }
    
    public boolean isTemporary() {
        return this.m_buildingUid <= 0L;
    }
    
    public long getBuildingUid() {
        return this.m_buildingUid;
    }
    
    public int getBuildingDefinitionId() {
        return this.m_buildingDefinitionId;
    }
    
    public int getItemId() {
        return this.m_itemId;
    }
    
    public void setItemId(final int itemId) {
        this.m_itemId = itemId;
        this.m_map = this.m_library.getEditorGroup(this.getEditorGroupId());
        if (this.m_map == null) {
            AbstractBuildingStruct.m_logger.error((Object)("pas de group d'id " + this.getEditorGroupId()));
        }
    }
    
    public int getEditorGroupId() {
        final AbstractBuildingDefinition def = this.getDefinition();
        if (def == null) {
            AbstractBuildingStruct.m_logger.error((Object)("pas de d\u00e9finition de batiment " + this.m_buildingDefinitionId));
            return 0;
        }
        return def.getEditorGroupId(this.m_itemId);
    }
    
    public AbstractBuildingDefinition getDefinition() {
        return HavenWorldDefinitionManager.INSTANCE.getBuilding((short)this.m_buildingDefinitionId);
    }
    
    public short getCellX() {
        return this.m_cellX;
    }
    
    public short getCellY() {
        return this.m_cellY;
    }
    
    @Override
    public String toString() {
        return "EditorGroupInstanceInfo{m_buildingUid=" + this.m_buildingUid + ", m_buildingDefinitionId=" + this.m_buildingDefinitionId + ", m_itemId=" + this.m_itemId + ", m_cellX=" + this.m_cellX + ", m_cellY=" + this.m_cellY + '}';
    }
    
    public Rect getCellBounds() {
        if (this.m_map == null) {
            return null;
        }
        return new Rect(this.m_cellX, this.m_cellX + this.m_map.getWidth(), this.m_cellY, this.m_cellY + this.m_map.getHeight());
    }
    
    public Rect getPartitionBounds() {
        if (this.m_map == null) {
            return null;
        }
        return MapConstants.getPartitionsFromCells(this.m_cellX, this.m_cellY, this.m_map.getWidth(), this.m_map.getHeight());
    }
    
    public int getBuildingType() {
        final BuildingCatalogEntry entry = this.getCatalogEntry();
        if (entry == null) {
            AbstractBuildingStruct.m_logger.error((Object)("pas d' entr\u00e9e pour " + this));
            return -1;
        }
        return entry.getBuildingType();
    }
    
    public final int getPathData(final int x, final int y, final CellPathData[] cellPathData, final int index) {
        if (this.m_map == null) {
            return 0;
        }
        final int originCellX = this.m_cellX;
        final int originCellY = this.m_cellY;
        final int originCellZ = 0;
        final int result = this.m_map.getPathData(x - originCellX, y - originCellY, cellPathData, index);
        for (int i = 0; i < result; ++i) {
            final CellPathData cellPathData2 = cellPathData[index + i];
            cellPathData2.m_x += originCellX;
            final CellPathData cellPathData3 = cellPathData[index + i];
            cellPathData3.m_y += originCellY;
            final CellPathData cellPathData4 = cellPathData[index + i];
            cellPathData4.m_z += 0;
        }
        return result;
    }
    
    public int getVisibilityData(final int x, final int y, final CellVisibilityData[] cellVisibilityData, final int index) {
        if (this.m_map == null) {
            return 0;
        }
        final int originCellX = this.m_cellX;
        final int originCellY = this.m_cellY;
        final int originCellZ = 0;
        final int result = this.m_map.getVisibilityData(x - originCellX, y - originCellY, cellVisibilityData, index);
        for (int i = 0; i < result; ++i) {
            final CellVisibilityData cellVisibilityData2 = cellVisibilityData[index + i];
            cellVisibilityData2.m_x += originCellX;
            final CellVisibilityData cellVisibilityData3 = cellVisibilityData[index + i];
            cellVisibilityData3.m_y += originCellY;
            final CellVisibilityData cellVisibilityData4 = cellVisibilityData[index + i];
            cellVisibilityData4.m_z += 0;
        }
        return result;
    }
    
    public boolean contains(final int x, final int y) {
        return this.m_map != null && this.m_map.isInMap(x - this.m_cellX, y - this.m_cellY);
    }
    
    public boolean isInBounds(final int x, final int y) {
        return this.m_map != null && this.m_map.isInBounds(x - this.m_cellX, y - this.m_cellY);
    }
    
    public boolean isCellBlocked(final int x, final int y) {
        return this.m_map == null || this.m_map.isCellBlocked(x - this.m_cellX, y - this.m_cellY);
    }
    
    public boolean isSteryl(final int x, final int y, final int z) {
        return this.m_map == null || this.m_map.isSteryl(x - this.m_cellX, y - this.m_cellY);
    }
    
    public T getModel() {
        return this.m_map;
    }
    
    public boolean isCellEmptyOrAltitudeEquals0(final int cellX, final int cellY) {
        return this.m_map != null && this.m_map.isCellEmptyOrAltitudeEquals0(cellX - this.m_cellX, cellY - this.m_cellY);
    }
    
    public BuildingCatalogEntry getCatalogEntry() {
        return BuildingDefinitionHelper.getEntryForBuilding((short)this.m_buildingDefinitionId);
    }
    
    public void toRaw(final ByteArray buffer) {
        buffer.putLong(this.m_buildingUid);
        buffer.putShort((short)this.m_buildingDefinitionId);
        buffer.putInt(this.m_itemId);
        buffer.putShort(this.m_cellX);
        buffer.putShort(this.m_cellY);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractBuildingStruct.class);
    }
}
