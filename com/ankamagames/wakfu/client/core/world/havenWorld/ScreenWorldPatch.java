package com.ankamagames.wakfu.client.core.world.havenWorld;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;

class ScreenWorldPatch extends ScreenWorld
{
    private static final Logger m_logger;
    static final int DEFAULT_GROUP_LAYER_OFFSET = 1;
    private final Modifier DEFAULT;
    private final IntObjectLightWeightMap<ScreenMap> m_allMaps;
    private int m_patchAltitudeOrderMax;
    private int m_patchLayerMax;
    private int m_groupLayerOffset;
    
    ScreenWorldPatch() {
        super();
        this.DEFAULT = new Modifier() {
            @Override
            public void modify(final ScreenElement elt) {
                if (elt.getAltitudeOrder() > ScreenWorldPatch.this.m_patchAltitudeOrderMax) {
                    ScreenWorldPatch.this.m_patchAltitudeOrderMax = elt.getAltitudeOrder();
                }
                if (elt.getLayerIndex() > ScreenWorldPatch.this.m_patchLayerMax) {
                    ScreenWorldPatch.this.m_patchLayerMax = Math.abs(elt.getLayerIndex());
                }
                elt.setGroupId(0);
                elt.setGroupKey(0);
            }
        };
        this.m_allMaps = new IntObjectLightWeightMap<ScreenMap>();
        this.m_patchLayerMax = -1;
        this.m_cache.clearMapOnRemove(false);
    }
    
    @Override
    protected ScreenMap loadMap(final String path, final int x, final int y) {
        assert this.m_allMaps.size() != 0;
        return this.m_allMaps.get(MathHelper.getIntFromTwoInt(x, y));
    }
    
    public void addPatch(final int cellOffsetX, final int cellOffsetY, final ClientPartitionPatch patch) {
        this.add(cellOffsetX, cellOffsetY, patch.getElements(), this.DEFAULT);
    }
    
    private void add(final int cellOffsetX, final int cellOffsetY, final GraphicalElement[] elements, final Modifier modifier) {
        final Rect rect = new Rect();
        final float screenwidth = 1024.0f;
        final float screenheight = 576.0f;
        for (final GraphicalElement element : elements) {
            final ScreenElement screenElt = createScreenElementFrom(cellOffsetX, cellOffsetY, element);
            modifier.modify(screenElt);
            if (screenElt.getCommonProperties() != null) {
                screenElt.computeLocation();
                screenElt.computeHashCode();
                screenElt.fillRectBounds(rect);
                final int mapLeft = MathHelper.fastFloor(rect.m_xMin / 1024.0f);
                final int mapTop = MathHelper.fastFloor(rect.m_yMin / 576.0f);
                final int mapRight = MathHelper.fastFloor(rect.m_xMax / 1024.0f);
                final int mapBottom = MathHelper.fastFloor(rect.m_yMax / 576.0f);
                assert mapLeft <= mapRight;
                assert mapTop <= mapBottom;
                for (int x = mapLeft; x <= mapRight; ++x) {
                    for (int y = mapTop; y <= mapBottom; ++y) {
                        ScreenWorld.addElementToMap(this.m_allMaps, (short)x, (short)y, screenElt);
                        screenElt.addReference();
                    }
                }
            }
            screenElt.removeReference();
        }
    }
    
    private static ScreenElement createScreenElementFrom(final int cellOffsetX, final int cellOffsetY, final GraphicalElement element) {
        final byte[] teint = element.getTeint();
        int type = 0;
        if (teint != null) {
            type |= 0x1;
        }
        final ScreenElement screenElement = new ScreenElement((byte)type);
        if (teint != null) {
            setTeint(screenElement, teint);
        }
        final int cellX = element.getCellX() + cellOffsetX;
        final int cellY = element.getCellY() + cellOffsetY;
        final short cellZ = element.getCellZ();
        screenElement.setWorldCoord(cellX, cellY, cellZ);
        screenElement.setProperties(element.getProperties());
        screenElement.setAltitudeOrder(element.getzOrder());
        screenElement.setHeight(element.getHeight());
        screenElement.setOccluder(element.isOccluder());
        screenElement.setGroupId(element.getGroupInstanceId());
        screenElement.setGroupKey(element.getGroupLayer());
        screenElement.setLayerIndex(element.getGroupLayer());
        return screenElement;
    }
    
    private static void setTeint(final ScreenElement screenElement, final byte[] teint) {
        screenElement.setTeint(convertColor(teint[0]), convertColor(teint[1]), convertColor(teint[2]));
    }
    
    private static float convertColor(final byte value) {
        return ScreenElement.getTeintValue(value);
    }
    
    public void onCreationDone() {
        for (int i = 0, size = this.m_allMaps.size(); i < size; ++i) {
            final ScreenMap screenMap = this.m_allMaps.getQuickValue(i);
            screenMap.onAllElementAdded();
            screenMap.computeBounds();
        }
    }
    
    public PartitionExistValidator getValidCoordinates() {
        final PartitionExistValidator coords = new PartitionExistValidator();
        for (int i = 0, size = this.m_allMaps.size(); i < size; ++i) {
            final ScreenMap screenMap = this.m_allMaps.getQuickValue(i);
            coords.add(screenMap.getX(), screenMap.getY());
        }
        return coords;
    }
    
    @Override
    public void clear() {
        for (int i = 0, size = this.m_allMaps.size(); i < size; ++i) {
            this.m_allMaps.getQuickValue(i).clear();
        }
        this.m_allMaps.clear();
        super.clear();
        this.m_groupLayerOffset = 0;
        this.m_patchAltitudeOrderMax = 0;
        this.m_patchLayerMax = 0;
    }
    
    public void addBuilding(@NotNull final BuildingStruct b) {
        final EditorGroupMap model = b.getModel();
        if (model == null) {
            ScreenWorldPatch.m_logger.error((Object)("pas de model associ\u00e9 \u00e0 l'instance de batiement " + b));
            return;
        }
        final int groupLayer = this.m_patchLayerMax + 1;
        final GroupModifier modifier = new GroupModifier(this.m_patchAltitudeOrderMax + 1, groupLayer, this.m_groupLayerOffset + 1, b.getEditorGroupId());
        this.add(b.getCellX(), b.getCellY(), model.getElements(), modifier);
        b.setLayer(groupLayer);
        final int layerCount = model.getLayerCount();
        this.m_patchLayerMax += layerCount;
        this.m_groupLayerOffset += layerCount;
        this.m_patchAltitudeOrderMax += modifier.getAltitudeOrderMax();
    }
    
    static {
        m_logger = Logger.getLogger((Class)ScreenWorldPatch.class);
    }
    
    private static class GroupModifier implements Modifier
    {
        private final int m_altitudeOrderOffset;
        private final int m_layerOffset;
        private final int m_groupLayerOffset;
        private final int m_groupId;
        private int m_altitudeOrderMax;
        
        GroupModifier(final int altitudeOrderOffset, final int layerOffset, final int groupLayerOffset, final int groupId) {
            super();
            this.m_altitudeOrderOffset = altitudeOrderOffset;
            this.m_layerOffset = layerOffset;
            this.m_groupLayerOffset = groupLayerOffset;
            this.m_groupId = groupId;
        }
        
        @Override
        public void modify(final ScreenElement elt) {
            if (elt.getAltitudeOrder() > this.m_altitudeOrderMax) {
                this.m_altitudeOrderMax = elt.getAltitudeOrder();
            }
            elt.setAltitudeOrder(elt.getAltitudeOrder() + this.m_altitudeOrderOffset);
            final short layerIndex = elt.getLayerIndex();
            elt.setLayerIndex((short)(Math.abs(layerIndex) + this.m_layerOffset));
            if (elt.getGroupId() != 0 && layerIndex != 0) {
                elt.setGroupId(this.m_groupId);
                final int groupOffset = (layerIndex > 0) ? this.m_groupLayerOffset : (-this.m_groupLayerOffset);
                elt.setGroupKey(layerIndex + groupOffset);
            }
            else {
                elt.setGroupKey(0);
            }
        }
        
        public int getAltitudeOrderMax() {
            return this.m_altitudeOrderMax;
        }
    }
    
    private interface Modifier
    {
        void modify(ScreenElement p0);
    }
}
