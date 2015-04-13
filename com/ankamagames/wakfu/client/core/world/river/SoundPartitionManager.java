package com.ankamagames.wakfu.client.core.world.river;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.interpolation.*;

public class SoundPartitionManager implements RenderProcessHandler
{
    public static final SoundPartitionManager INSTANCE;
    private static final Logger m_logger;
    private static final float INTERPOLATION_MOVEMENT_LIMIT = 0.01f;
    private static final float DEFAULT_MOVE_SPEED = 2.1f;
    private static final byte NONE_STATE = 0;
    private static final byte FOUND_STATE = 1;
    private IntObjectLightWeightMap<SoundPartition> m_partitions;
    private final IntObjectLightWeightMap<SoundZone> m_zones;
    private int m_mapX;
    private int m_mapY;
    private IsoCamera m_camera;
    private static final Vector3 VECTOR;
    
    private SoundPartitionManager() {
        super();
        this.m_partitions = new IntObjectLightWeightMap<SoundPartition>();
        this.m_zones = new IntObjectLightWeightMap<SoundZone>();
        this.m_mapX = Integer.MIN_VALUE;
        this.m_mapY = Integer.MIN_VALUE;
        for (final SoundZoneType type : SoundZoneType.values()) {
            this.m_zones.put(type.getId(), new SoundZone(type));
        }
    }
    
    public void setMapXY(final short mapX, final short mapY) {
        if (this.m_camera == null) {
            this.m_camera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
        }
        if (this.m_mapX == mapX && this.m_mapY == mapY) {
            return;
        }
        this.m_mapX = mapX;
        this.m_mapY = mapY;
        final IntObjectLightWeightMap<SoundPartition> partitions = new IntObjectLightWeightMap<SoundPartition>();
        for (short i = (short)(mapX - 1), maxX = (short)(mapX + 1); i <= maxX; ++i) {
            for (short j = (short)(mapY - 1), maxY = (short)(mapY + 1); j <= maxY; ++j) {
                final int index = this.mapCoordsToIndex(i, j);
                SoundPartition part = this.m_partitions.get(index);
                if (part == null) {
                    try {
                        part = new SoundPartition(i, j);
                    }
                    catch (Exception e) {
                        continue;
                    }
                }
                partitions.put(index, part);
            }
        }
        this.m_partitions = partitions;
        for (int k = this.m_zones.size() - 1; k >= 0; --k) {
            this.m_zones.getQuickValue(k).precompute(this.m_partitions);
        }
    }
    
    public void setCameraTargetPosition(final int x, final int y) {
        for (int i = this.m_zones.size() - 1; i >= 0; --i) {
            final SoundZone zone = this.m_zones.getQuickValue(i);
            if (zone.m_hasSomeAround) {
                if (this.getWeightAt(zone, x, y) == 0) {
                    this.computeVectorFromWeight(zone, x, y);
                }
                else {
                    this.computeVectorFromPosition(zone, x, y);
                }
            }
            else if (zone.m_loadingState != 0) {
                zone.m_loadingState = 0;
                WakfuSoundManager.getInstance().onEvent(new GeographySoundEvent(zone.m_type.getSource(), zone.m_type.getEventType(), 0));
                SoundPartitionManager.m_logger.debug((Object)("Aucune " + zone.m_type + " alentour"));
            }
        }
    }
    
    private byte getWeightAt(final SoundZone zone, final int x, final int y) {
        final int index = this.mapCoordsToIndex((short)PartitionConstants.getPartitionXFromCellX(x), (short)PartitionConstants.getPartitionYFromCellY(y));
        final SoundPartition partition = this.m_partitions.get(index);
        if (partition == null) {
            return 0;
        }
        final int xInMap = (x % 18 + 18) % 18;
        final int yInMap = (y % 18 + 18) % 18;
        return partition.getWeightAt(zone.m_type.getId(), xInMap, yInMap);
    }
    
    private void computeVectorFromWeight(final SoundZone zone, final int x, final int y) {
        final int bigSubCenterX = SoundPartition.worldCoordToWorldBigSub(x);
        final int bigSubCenterY = SoundPartition.worldCoordToWorldBigSub(y);
        int vectX = 0;
        int vectY = 0;
        int strength = 0;
        for (int deltaX = -1; deltaX < 2; ++deltaX) {
            for (int deltaY = -1; deltaY < 2; ++deltaY) {
                final int worldBigSubX = bigSubCenterX + deltaX;
                final int worldBigSubY = bigSubCenterY + deltaY;
                final int mapIndex = this.mapCoordsToIndex(SoundPartition.worldBigSubCoordToMap(worldBigSubX), SoundPartition.worldBigSubCoordToMap(worldBigSubY));
                final SoundPartition partition = this.m_partitions.get(mapIndex);
                if (partition != null) {
                    final byte weight = partition.getWeightAtWorldBigCoord(zone.m_type.getId(), worldBigSubX, worldBigSubY);
                    if (weight > 0) {
                        vectX += deltaX;
                        vectY += deltaY;
                        strength += weight;
                    }
                }
            }
        }
        SoundPartitionManager.VECTOR.set(vectX, vectY, 0.0f);
        SoundPartitionManager.VECTOR.normalizeCurrent();
        final int centerX = SoundPartition.worldBigSubToWorldCoord(bigSubCenterX);
        final int centerY = SoundPartition.worldBigSubToWorldCoord(bigSubCenterY);
        SoundPartitionManager.m_logger.debug((Object)("[weight]OrigPos= " + x + ", " + y));
        SoundPartitionManager.m_logger.debug((Object)("[weight]CenterPos= " + centerX + ", " + centerY));
        if (zone.m_loadingState == 0) {
            zone.m_positionX.set(centerX + SoundPartitionManager.VECTOR.getX() * 10.0f);
            zone.m_positionY.set(centerY + SoundPartitionManager.VECTOR.getY() * 10.0f);
        }
        else {
            zone.m_positionX.setEnd(centerX + SoundPartitionManager.VECTOR.getX() * 10.0f);
            zone.m_positionY.setEnd(centerY + SoundPartitionManager.VECTOR.getY() * 10.0f);
        }
        SoundPartitionManager.m_logger.debug((Object)("[weight]Pos= " + zone.m_positionX.getEnd() + ", " + zone.m_positionY.getEnd()));
        if (strength == 0) {
            if (zone.m_loadingState != 0) {
                zone.m_loadingState = 0;
                WakfuSoundManager.getInstance().onEvent(new GeographySoundEvent(zone.m_type.getSource(), zone.m_type.getEventType(), 0));
                SoundPartitionManager.m_logger.debug((Object)("Aucune " + zone.m_type + " alentour"));
            }
        }
        else if (zone.m_loadingState != 1) {
            zone.m_loadingState = 1;
            WakfuSoundManager.getInstance().onEvent(new GeographySoundEvent(zone.m_type.getSource(), zone.m_type.getEventType(), 75));
            SoundPartitionManager.m_logger.debug((Object)("D\u00e9tection d'une " + zone.m_type + "\u00e9loign\u00e9e - Force=" + strength));
        }
    }
    
    private void computeVectorFromPosition(final SoundZone zone, final int x, final int y) {
        final int subCenterX = SoundPartition.worldCoordToWorldSub(x);
        final int subCenterY = SoundPartition.worldCoordToWorldSub(y);
        int vectX = 0;
        int vectY = 0;
        int strength = 0;
        for (int deltaX = -1; deltaX < 2; ++deltaX) {
            for (int deltaY = -1; deltaY < 2; ++deltaY) {
                final int worldSubX = subCenterX + deltaX;
                final int worldSubY = subCenterY + deltaY;
                final int mapIndex = this.mapCoordsToIndex(SoundPartition.worldSubCoordToMap(worldSubX), SoundPartition.worldSubCoordToMap(worldSubY));
                final SoundPartition partition = this.m_partitions.get(mapIndex);
                if (partition != null && partition.hasAtWorldSubCoord(zone.m_type.getId(), worldSubX, worldSubY)) {
                    vectX += deltaX;
                    vectY += deltaY;
                    ++strength;
                }
            }
        }
        SoundPartitionManager.VECTOR.set(vectX, vectY, 0.0f);
        SoundPartitionManager.VECTOR.normalizeCurrent();
        final int centerX = SoundPartition.worldSubToWorldCoord(subCenterX);
        final int centerY = SoundPartition.worldSubToWorldCoord(subCenterY);
        SoundPartitionManager.m_logger.debug((Object)("[pos]OrigPos= " + x + ", " + y));
        SoundPartitionManager.m_logger.debug((Object)("[pos]CenterPos= " + centerX + ", " + centerY));
        if (zone.m_loadingState == 0) {
            zone.m_positionX.set(centerX + SoundPartitionManager.VECTOR.getX() * (10 - strength));
            zone.m_positionY.set(centerY + SoundPartitionManager.VECTOR.getY() * (10 - strength));
        }
        else {
            zone.m_positionX.setEnd(centerX + SoundPartitionManager.VECTOR.getX() * (10 - strength));
            zone.m_positionY.setEnd(centerY + SoundPartitionManager.VECTOR.getY() * (10 - strength));
        }
        SoundPartitionManager.m_logger.debug((Object)("[pos]Pos= " + zone.m_positionX.getEnd() + ", " + zone.m_positionY.getEnd()));
        if (zone.m_loadingState != 1) {
            zone.m_loadingState = 1;
            WakfuSoundManager.getInstance().onEvent(new GeographySoundEvent(zone.m_type.getSource(), zone.m_type.getEventType(), 75));
            SoundPartitionManager.m_logger.debug((Object)("D\u00e9tection d'un " + zone.m_type + " proche - Force=" + strength));
        }
    }
    
    public void clear() {
        this.m_partitions.clear();
        for (int i = this.m_zones.size() - 1; i >= 0; --i) {
            final SoundZone zone = this.m_zones.getQuickValue(i);
            if (zone.m_loadingState != 0) {
                zone.m_loadingState = 0;
                WakfuSoundManager.getInstance().onEvent(new GeographySoundEvent(zone.m_type.getSource(), zone.m_type.getEventType(), 0));
            }
        }
        this.m_mapX = Integer.MIN_VALUE;
        this.m_mapY = Integer.MIN_VALUE;
    }
    
    private int mapCoordsToIndex(final short mapX, final short mapY) {
        return MathHelper.getIntFromTwoShort(mapX, mapY);
    }
    
    @Override
    public void process(final IsoWorldScene isoWorldScene, final int deltaTime) {
        for (int i = this.m_zones.size() - 1; i >= 0; --i) {
            final SoundZone zone = this.m_zones.getQuickValue(i);
            zone.m_positionX.process(deltaTime);
            zone.m_positionY.process(deltaTime);
        }
    }
    
    @Override
    public void prepareBeforeRendering(final IsoWorldScene isoWorldScene, final float centerScreenIsoWorldX, final float centerScreenIsoWorldY) {
    }
    
    final float getX(final SoundZoneType type) {
        final SoundZone zone = this.m_zones.get(type.getId());
        if (zone == null) {
            return 0.0f;
        }
        return zone.m_positionX.getValue();
    }
    
    final float getY(final SoundZoneType type) {
        final SoundZone zone = this.m_zones.get(type.getId());
        if (zone == null) {
            return 0.0f;
        }
        return zone.m_positionY.getValue();
    }
    
    final float getZ(final SoundZoneType type) {
        return this.m_camera.getListenerPosition().getZ();
    }
    
    static {
        INSTANCE = new SoundPartitionManager();
        m_logger = Logger.getLogger((Class)SoundPartitionManager.class);
        VECTOR = new Vector3();
    }
    
    private class SoundZone
    {
        private final SoundZoneType m_type;
        private final Interpolator m_positionX;
        private final Interpolator m_positionY;
        private byte m_loadingState;
        private boolean m_hasSomeAround;
        
        private SoundZone(final SoundZoneType type) {
            super();
            this.m_positionX = new Interpolator();
            this.m_positionY = new Interpolator();
            this.m_loadingState = 0;
            this.m_hasSomeAround = false;
            this.m_type = type;
            this.m_positionX.setSpeed(2.1f);
            this.m_positionX.setDelta(0.01f);
            this.m_positionY.setSpeed(2.1f);
            this.m_positionY.setDelta(0.01f);
        }
        
        public void precompute(final IntObjectLightWeightMap<SoundPartition> partitions) {
            for (int i = SoundPartitionManager.this.m_partitions.size() - 1; i >= 0; --i) {
                this.m_hasSomeAround |= SoundPartitionManager.this.m_partitions.getQuickValue(i).hasType(this.m_type.getId());
            }
        }
    }
}
