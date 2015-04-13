package com.ankamagames.wakfu.common.game.world.havenWorld.validator;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class InsertPatchValidator extends ModificationValidator<AbstractHavenWorldTopology>
{
    private static final Logger m_logger;
    private final PartitionPatchLibrary m_patchLibrary;
    
    public InsertPatchValidator(final AbstractHavenWorldTopology havenWorld, final PartitionPatchLibrary patchLibrary) {
        super(havenWorld);
        this.m_patchLibrary = patchLibrary;
    }
    
    public boolean validate(final short patchId, final int patchX, final int patchY) {
        if (!((AbstractHavenWorldTopology)this.m_dataProvider).isEditablePatch(patchX, patchY)) {
            this.addPatchConflict(patchId, patchX, patchY);
            return false;
        }
        final AbstractHavenWorldTopology world = ((AbstractHavenWorldTopology)this.m_dataProvider).copy();
        world.setPatchId(patchX, patchY, patchId);
        if (!this.hasOnePatchNeighbor(world, patchX, patchY)) {
            this.addPatchConflict(patchId, patchX, patchY);
            return false;
        }
        this.validateBuildings(world, patchX, patchY);
        return this.hasErrors();
    }
    
    private void addPatchConflict(final short patchId, final int patchX, final int patchY) {
        final PatchCatalogEntry catalogEntry = HavenWorldDefinitionManager.INSTANCE.getPatchCatalogEntry(patchId);
        this.addConflict(new PatchItem(catalogEntry, patchX, patchY));
    }
    
    private boolean hasOnePatchNeighbor(final AbstractHavenWorldTopology world, final int patchX, final int patchY) {
        return PartitionPatch.isEditable(world.getPatchId(patchX - 1, patchY)) || PartitionPatch.isEditable(world.getPatchId(patchX + 1, patchY)) || PartitionPatch.isEditable(world.getPatchId(patchX, patchY - 1)) || PartitionPatch.isEditable(world.getPatchId(patchX, patchY + 1));
    }
    
    private void validateBuildings(final AbstractHavenWorldTopology world, final int patchX, final int patchY) {
        final int mapX = AbstractHavenWorldTopology.patchCoordXToPartition(patchX);
        final int mapY = AbstractHavenWorldTopology.patchCoordYToPartition(patchY);
        for (final AbstractBuildingStruct b : world.getBuildingsInMap((short)mapX, (short)mapY)) {
            if (!this.buildingValid(world, b)) {
                this.addConflict(new BuildingItem(b));
            }
        }
    }
    
    public boolean buildingValid(final AbstractHavenWorldTopology world, final AbstractBuildingStruct b) {
        final Rect cellBounds = b.getCellBounds();
        final int minX = cellBounds.m_xMin;
        final int minY = cellBounds.m_yMin;
        final int maxX = cellBounds.m_xMax;
        for (int maxY = cellBounds.m_yMax, cellY = minY; cellY < maxY; ++cellY) {
            for (int cellX = minX; cellX < maxX; ++cellX) {
                if (this.patchIsBlocking(world, cellX, cellY)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean patchIsBlocking(final AbstractHavenWorldTopology world, final int cellX, final int cellY) {
        return patchIsBlocking(this.m_patchLibrary, world, cellX, cellY);
    }
    
    static boolean patchIsBlocking(final PartitionPatchLibrary patchLibrary, final AbstractHavenWorldTopology world, final int cellX, final int cellY) {
        final int patchX = PartitionPatch.getPatchCoordFromCellX(cellX);
        final int patchY = PartitionPatch.getPatchCoordFromCellY(cellY);
        final int patchId = world.getPatchId(patchX, patchY);
        final PartitionPatch patch = patchLibrary.getPatch(patchId);
        final int patchCellX = patchX * 9;
        final int patchCellY = patchY * 9;
        final int x = cellX - patchCellX;
        final int y = cellY - patchCellY;
        return patch == null || patch.isBlocked(x, y) || patch.getAltitude(x, y) != 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)InsertPatchValidator.class);
    }
}
