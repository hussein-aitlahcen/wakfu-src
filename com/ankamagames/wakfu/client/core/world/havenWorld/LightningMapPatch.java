package com.ankamagames.wakfu.client.core.world.havenWorld;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.world.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;

class LightningMapPatch extends LightningMap implements ConvertFromPatch
{
    private static final Logger m_logger;
    private Preparator m_preparator;
    
    @Override
    public void fromPatch(final PartitionPatch patchTopLeft, final PartitionPatch patchTopRight, final PartitionPatch patchBottomLeft, final PartitionPatch patchBottomRight) {
        this.m_preparator = new Preparator(patchTopLeft, patchTopRight, patchBottomLeft, patchBottomRight);
    }
    
    @Override
    public void setAttachedBuilding(@NotNull final AbstractBuildingStruct[] attachedBuildings) {
        this.m_preparator.setAttachedBuilding(attachedBuildings);
        this.createFrom(this.m_preparator);
        this.m_preparator = null;
    }
    
    private void createFrom(final Preparator preparator) {
        this.m_layerColors = new CellLightDef[this.m_preparator.getLayerCount() * 324];
        final int cellOffsetX = this.getCellCoordX();
        final int cellOffsetY = this.getCellCoordY();
        this.mergeColors(preparator.topLeft, cellOffsetX, cellOffsetY);
        this.mergeColors(preparator.topRight, cellOffsetX + 9, cellOffsetY);
        this.mergeColors(preparator.bottomLeft, cellOffsetX, cellOffsetY + 9);
        this.mergeColors(preparator.bottomRight, cellOffsetX + 9, cellOffsetY + 9);
        for (final AbstractBuildingStruct building : preparator.m_attachedBuildings) {
            final BuildingStruct b = (BuildingStruct)building;
            final LightData lightData = b.getModel().getLightData();
            this.mergeColors(building.getCellX(), building.getCellY(), b.getLayer(), lightData);
        }
    }
    
    private void mergeColors(final ClientPartitionPatch patch, final int cellOffsetX, final int cellOffsetY) {
        if (patch == null) {
            return;
        }
        this.mergeColors(cellOffsetX, cellOffsetY, 0, patch.getLightData());
    }
    
    private void mergeColors(final int dx, final int dy, final int layerOffset, final LightData lightData) {
        final ArrayList<CellLightDef> cellLightDef = lightData.getCellLightDef();
        if (cellLightDef.isEmpty()) {
            return;
        }
        for (int i = 0, size = cellLightDef.size(); i < size; ++i) {
            final CellLightDef lightDef = cellLightDef.get(i);
            final int index = this.m_cellLightDef.indexOf(lightDef);
            if (index == -1) {
                this.m_cellLightDef.add(lightDef);
            }
        }
        final int layerCount = lightData.getLightLayerCount();
        for (int y = 0; y < lightData.getHeight(); ++y) {
            final int ty = y + dy;
            if (ty >= this.getCellCoordY()) {
                if (ty < this.getCellCoordY() + 18) {
                    for (int x = 0; x < lightData.getWidth(); ++x) {
                        final int tx = x + dx;
                        if (tx >= this.getCellCoordX()) {
                            if (tx < this.getCellCoordX() + 18) {
                                for (int l = 0; l < layerCount; ++l) {
                                    CellLightDef colors = lightData.getLayerColors(x, y, l);
                                    if (colors != null) {
                                        final int index2 = this.m_cellLightDef.indexOf(colors);
                                        if (index2 != -1) {
                                            colors = this.m_cellLightDef.get(index2);
                                        }
                                    }
                                    this.m_layerColors[this.getHashCode(tx, ty, layerOffset + l)] = colors;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)LightningMapPatch.class);
    }
    
    private static class Preparator
    {
        final ClientPartitionPatch topLeft;
        final ClientPartitionPatch topRight;
        final ClientPartitionPatch bottomLeft;
        final ClientPartitionPatch bottomRight;
        AbstractBuildingStruct[] m_attachedBuildings;
        
        Preparator(final PartitionPatch patchTopLeft, final PartitionPatch patchTopRight, final PartitionPatch patchBottomLeft, final PartitionPatch patchBottomRight) {
            super();
            this.topLeft = (ClientPartitionPatch)patchTopLeft;
            this.topRight = (ClientPartitionPatch)patchTopRight;
            this.bottomLeft = (ClientPartitionPatch)patchBottomLeft;
            this.bottomRight = (ClientPartitionPatch)patchBottomRight;
        }
        
        void setAttachedBuilding(@NotNull final AbstractBuildingStruct[] attachedBuildings) {
            this.m_attachedBuildings = attachedBuildings;
        }
        
        private int getLayerCount(final ClientPartitionPatch patch) {
            return (patch == null) ? 0 : patch.getLightData().getLightLayerCount();
        }
        
        int getLayerCount() {
            int maxLayerCount;
            final int layerCount = maxLayerCount = MathHelper.max(this.getLayerCount(this.topLeft), this.getLayerCount(this.topRight), this.getLayerCount(this.bottomLeft), this.getLayerCount(this.bottomRight));
            for (final AbstractBuildingStruct building : this.m_attachedBuildings) {
                final BuildingStruct b = (BuildingStruct)building;
                final int maxLayer = b.getModel().getLayerCount() + b.getLayer();
                if (maxLayer > maxLayerCount) {
                    maxLayerCount = maxLayer;
                }
            }
            return maxLayerCount + 1;
        }
    }
}
