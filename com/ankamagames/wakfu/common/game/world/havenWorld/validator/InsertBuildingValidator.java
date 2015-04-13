package com.ankamagames.wakfu.common.game.world.havenWorld.validator;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.building.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;

public class InsertBuildingValidator extends ModificationValidator<AbstractHavenWorldTopology>
{
    private static final Logger m_logger;
    private final PartitionPatchLibrary m_patchLibrary;
    
    public InsertBuildingValidator(final AbstractHavenWorldTopology havenWorld, final PartitionPatchLibrary patchLibrary) {
        super(havenWorld);
        this.m_patchLibrary = patchLibrary;
    }
    
    public boolean validate(final AbstractBuildingStruct info) {
        this.checkBuildingsCells(info);
        this.checkPatches(info);
        return !this.hasErrors();
    }
    
    private void checkBuildingsCells(final AbstractBuildingStruct info) {
        final AbstractEditorGroupMap model = info.getModel();
        final AbstractBuildingDefinition def = info.getDefinition();
        final boolean isDeco = def.isDecoOnly();
        final int x = info.getCellX();
        final int y = info.getCellY();
        final int width = model.getWidth();
        final int height = model.getHeight();
        AbstractBuildingStruct[] buildingsInMap = null;
        short lastMapX = -32768;
        short lastMapY = -32768;
        for (int cellY = y; cellY < y + height; ++cellY) {
            for (int cellX = x; cellX < x + width; ++cellX) {
                final boolean isEmptyCell = model.isCellEmptyOrAltitudeEquals0(cellX - x, cellY - y);
                if (!isDeco || !isEmptyCell) {
                    final short mapx = (short)MapConstants.getMapCoordFromCellX(cellX);
                    final short mapy = (short)MapConstants.getMapCoordFromCellY(cellY);
                    if (buildingsInMap == null || lastMapX != mapx || lastMapY != mapy) {
                        buildingsInMap = ((AbstractHavenWorldTopology)this.m_dataProvider).getBuildingsInMap(mapx, mapy);
                        lastMapX = mapx;
                        lastMapY = mapy;
                    }
                    for (final AbstractBuildingStruct building : buildingsInMap) {
                        if (building.getBuildingUid() != info.getBuildingUid()) {
                            final AbstractBuildingDefinition buildingDef = building.getDefinition();
                            if (building.isInBounds(cellX, cellY)) {
                                if (!isDeco && !buildingDef.isDecoOnly()) {
                                    this.addConflict(new BuildingItem(building));
                                }
                                else if (!isEmptyCell) {
                                    if (building.contains(cellX, cellY)) {
                                        if (!isDeco || !building.isCellEmptyOrAltitudeEquals0(cellX, cellY)) {
                                            this.addConflict(new BuildingItem(building));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void checkPatches(final AbstractBuildingStruct info) {
        final Rect rect = info.getCellBounds();
        for (int cellY = rect.m_yMin; cellY < rect.m_yMax; ++cellY) {
            for (int cellX = rect.m_xMin; cellX < rect.m_xMax; ++cellX) {
                final short patchX = AbstractHavenWorldTopology.patchXFromCellX(cellX);
                final short patchY = AbstractHavenWorldTopology.patchYFromCellY(cellY);
                final short patchId = ((AbstractHavenWorldTopology)this.m_dataProvider).getPatchId(patchX, patchY);
                if (!((AbstractHavenWorldTopology)this.m_dataProvider).isEditablePatch(patchX, patchY)) {
                    this.addConflict(patchId, patchX, patchY);
                }
                else if (InsertPatchValidator.patchIsBlocking(this.m_patchLibrary, (AbstractHavenWorldTopology)this.m_dataProvider, cellX, cellY)) {
                    this.addConflict(patchId, patchX, patchY);
                }
            }
        }
    }
    
    private void addConflict(final short patchId, final short patchX, final short patchY) {
        final PatchCatalogEntry catalogEntry = HavenWorldDefinitionManager.INSTANCE.getPatchCatalogEntry(patchId);
        this.addConflict(new PatchItem(catalogEntry, patchX, patchY));
    }
    
    static {
        m_logger = Logger.getLogger((Class)InsertBuildingValidator.class);
    }
}
