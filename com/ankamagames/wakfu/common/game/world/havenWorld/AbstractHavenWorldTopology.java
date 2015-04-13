package com.ankamagames.wakfu.common.game.world.havenWorld;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.building.*;
import com.ankamagames.wakfu.common.alea.topology.*;
import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;

public abstract class AbstractHavenWorldTopology
{
    private static final Logger m_logger;
    public static final int START_EDITABLE_PATCH_Y = -6;
    private static final int PARTITION_BASE_WIDTH = 4;
    private static final int PARTITION_BASE_HEIGHT = 4;
    protected final short m_worldId;
    private int m_originX;
    private int m_originY;
    private int m_width;
    private int m_height;
    private short[][] m_patchIds;
    private final TLongObjectHashMap<AbstractBuildingStruct> m_buildings;
    private final BuildingMapSelector m_buildingMapSelector;
    
    protected AbstractHavenWorldTopology(final short worldId, final int partitionWidth, final int partitionHeight) {
        super();
        this.m_buildings = new TLongObjectHashMap<AbstractBuildingStruct>();
        this.m_buildingMapSelector = new BuildingMapSelector(this.m_buildings);
        this.m_worldId = worldId;
        this.create(0, 0, partitionWidth, partitionHeight);
        this.fill((short)0);
    }
    
    protected AbstractHavenWorldTopology(final short worldId) {
        this(worldId, 4, 4);
    }
    
    protected AbstractHavenWorldTopology(final AbstractHavenWorldTopology base) {
        super();
        this.m_buildings = new TLongObjectHashMap<AbstractBuildingStruct>();
        this.m_buildingMapSelector = new BuildingMapSelector(this.m_buildings);
        this.m_worldId = base.m_worldId;
        this.m_originX = base.m_originX;
        this.m_originY = base.m_originY;
        this.m_width = base.m_width;
        this.m_height = base.m_height;
        this.m_patchIds = base.m_patchIds.clone();
        for (int i = 0, length = base.m_patchIds.length; i < length; ++i) {
            this.m_patchIds[i] = base.m_patchIds[i].clone();
        }
        this.m_buildings.putAll(base.m_buildings);
        this.m_buildingMapSelector.cleanCache();
    }
    
    public boolean isEditablePatch(final int patchX, final int patchY) {
        return this.getEditableWorldBounds().contains(patchX, patchY);
    }
    
    public Rect getEditableWorldBounds() {
        return new Rect(this.getOriginX() + 2, this.getMaxX() - 2, this.getOriginY() + 2, this.getMaxY() - 8);
    }
    
    public final int getOriginX() {
        return this.m_originX;
    }
    
    public final int getOriginY() {
        return this.m_originY;
    }
    
    public final int getWidth() {
        return this.m_width;
    }
    
    public final int getHeight() {
        return this.m_height;
    }
    
    public final int getMaxX() {
        return this.m_originX + this.m_width - 1;
    }
    
    public final int getMaxY() {
        return this.m_originY + this.m_height - 1;
    }
    
    public final void create(final int partitionOriginX, final int partitionOriginY, final int partitionWidth, final int partitionHeight) {
        final int width = partitionToPatchCoordX(partitionWidth);
        final int height = partitionToPatchCoordY(partitionHeight);
        this.m_patchIds = new short[width][height];
        this.m_width = width;
        this.m_height = height;
        this.m_originX = partitionToPatchCoordX(partitionOriginX);
        this.m_originY = partitionToPatchCoordY(partitionOriginY);
    }
    
    protected final short[][] createCopy() {
        final short[][] copy = new short[this.m_width][];
        for (int x = 0; x < this.m_width; ++x) {
            copy[x] = this.m_patchIds[x].clone();
        }
        return copy;
    }
    
    protected final void fill(final short value) {
        for (int x = 0; x < this.m_width; ++x) {
            for (int y = 0; y < this.m_height; ++y) {
                this.m_patchIds[x][y] = value;
            }
        }
    }
    
    public TopologyMapPatch createTopologyMap(final short mapCoordX, final short mapCoordY) {
        final TopologyMapPatch map = new TopologyMapPatch(mapCoordX, mapCoordY);
        this.initializeMap(mapCoordX, mapCoordY, map);
        return map;
    }
    
    protected final void initializeMap(final short mapCoordX, final short mapCoordY, final ConvertFromPatch map) {
        final int x = partitionToPatchCoordX(mapCoordX);
        final int y = partitionToPatchCoordY(mapCoordY);
        map.fromPatch(this.getPatch(x, y), this.getPatch(x + 1, y), this.getPatch(x, y + 1), this.getPatch(x + 1, y + 1));
        this.refreshAttachedBuildings(mapCoordX, mapCoordY, map);
    }
    
    public void refreshAttachedBuildings(final short mapCoordX, final short mapCoordY, final ConvertFromPatch map) {
        map.setAttachedBuilding(this.m_buildingMapSelector.getBuildingsInMap(mapCoordX, mapCoordY));
    }
    
    public final boolean mapExists(final short mapCoordX, final short mapCoordY) {
        final int x = partitionToPatchCoordX(mapCoordX);
        final int y = partitionToPatchCoordY(mapCoordY);
        return x >= this.getOriginX() && x <= this.getMaxX() && y >= this.getOriginY() && y <= this.getMaxY() && (this.getPatchId(x, y) != PartitionPatch.EMPTY || this.getPatchId(x + 1, y) != PartitionPatch.EMPTY || this.getPatchId(x, y + 1) != PartitionPatch.EMPTY || this.getPatchId(x + 1, y + 1) != PartitionPatch.EMPTY);
    }
    
    public final void setPatchId(final int patchCoordX, final int patchCoordY, final short patchId) {
        this.m_patchIds[patchCoordX - this.m_originX][patchCoordY - this.m_originY] = patchId;
    }
    
    public short getPatchId(final int patchCoordX, final int patchCoordY) {
        return this.m_patchIds[patchCoordX - this.m_originX][patchCoordY - this.m_originY];
    }
    
    public void setPartitionPatchIds(final short partitionX, final short partitionY, final short topLeftPatch, final short topRightPatch, final short bottomLeftPatch, final short bottomRightPatch) {
        final int patchCoordX = partitionToPatchCoordX(partitionX);
        final int patchCoordY = partitionToPatchCoordY(partitionY);
        this.setPatchId(patchCoordX, patchCoordY, topLeftPatch);
        this.setPatchId(patchCoordX + 1, patchCoordY, topRightPatch);
        this.setPatchId(patchCoordX, patchCoordY + 1, bottomLeftPatch);
        this.setPatchId(patchCoordX + 1, patchCoordY + 1, bottomRightPatch);
    }
    
    protected final PartitionPatch getPatch(final int x, final int y) {
        final int patchId = this.getPatchId(x, y);
        return (patchId == PartitionPatch.EMPTY) ? null : this.getPatch(patchId);
    }
    
    public abstract PartitionPatch getPatch(final int p0);
    
    public short getWorldId() {
        return this.m_worldId;
    }
    
    public void addBuilding(final AbstractBuildingStruct info) {
        this.removeBuilding(info.getBuildingUid());
        this.m_buildings.put(info.getBuildingUid(), info);
        this.m_buildingMapSelector.cleanCache();
    }
    
    public AbstractBuildingStruct removeBuilding(final long buildingUid) {
        final AbstractBuildingStruct remove = this.m_buildings.remove(buildingUid);
        if (remove != null) {
            this.m_buildingMapSelector.cleanCache();
        }
        return remove;
    }
    
    public static short patchXFromCellX(final int cellX) {
        final int partitionX = MathHelper.fastFloor(cellX / 9.0f);
        return MathHelper.ensureShort(partitionX);
    }
    
    public static short patchYFromCellY(final int cellY) {
        final int partitionY = MathHelper.fastFloor(cellY / 9.0f);
        return MathHelper.ensureShort(partitionY);
    }
    
    public static short patchCoordXToPartition(final int patchX) {
        final int partitionX = MathHelper.fastFloor(patchX / 2.0f);
        return MathHelper.ensureShort(partitionX);
    }
    
    public static short patchCoordYToPartition(final int patchY) {
        final int partitionY = MathHelper.fastFloor(patchY / 2.0f);
        return MathHelper.ensureShort(partitionY);
    }
    
    public static short partitionToPatchCoordX(final int partitionX) {
        final int patchX = partitionX * 2;
        return MathHelper.ensureShort(patchX);
    }
    
    public static short partitionToPatchCoordY(final int partitionY) {
        final int patchY = partitionY * 2;
        return MathHelper.ensureShort(patchY);
    }
    
    public static short topLeftPatchCoordX(final int partitionX) {
        return partitionToPatchCoordX(partitionX);
    }
    
    public static short topLeftPatchCoordY(final int partitionY) {
        return partitionToPatchCoordY(partitionY);
    }
    
    public static short topRightPatchCoordX(final int partitionX) {
        final int patchX = partitionToPatchCoordX(partitionX) + 1;
        return MathHelper.ensureShort(patchX);
    }
    
    public static short topRightPatchCoordY(final int partitionY) {
        return partitionToPatchCoordY(partitionY);
    }
    
    public static short bottomLeftPatchCoordX(final int partitionX) {
        return partitionToPatchCoordX(partitionX);
    }
    
    public static short bottomLeftPatchCoordY(final int partitionY) {
        final int patchY = partitionToPatchCoordY(partitionY) + 1;
        return MathHelper.ensureShort(patchY);
    }
    
    public static short bottomRightPatchCoordX(final int partitionX) {
        final int patchX = partitionToPatchCoordX(partitionX) + 1;
        return MathHelper.ensureShort(patchX);
    }
    
    public static short bottomRightPatchCoordY(final int partitionY) {
        final int patchY = partitionToPatchCoordY(partitionY) + 1;
        return MathHelper.ensureShort(patchY);
    }
    
    public AbstractBuildingStruct[] getBuildingsInMap(final short mapCoordX, final short mapCoordY) {
        return this.m_buildingMapSelector.getBuildingsInMap(mapCoordX, mapCoordY);
    }
    
    public boolean foreachBuildings(final TObjectProcedure<AbstractBuildingStruct> procedure) {
        return this.m_buildings.forEachValue(procedure);
    }
    
    public abstract AbstractHavenWorldTopology copy();
    
    public AbstractBuildingStruct equipBuilding(final long buildingUid, final int equippedItemId) {
        final AbstractBuildingStruct b = this.m_buildings.get(buildingUid);
        if (b == null) {
            AbstractHavenWorldTopology.m_logger.error((Object)("le batiment " + buildingUid + " n'existe pas"));
            return null;
        }
        b.setItemId(equippedItemId);
        return b;
    }
    
    public int getPartitionCost() {
        return 250000;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractHavenWorldTopology.class);
    }
}
