package com.ankamagames.wakfu.client.core.world.havenWorld;

import com.ankamagames.wakfu.client.alea.environment.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.baseImpl.common.clientAndServer.alea.environment.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.framework.kernel.core.maths.*;

class WakfuClientEnvironmentMapPatch extends WakfuClientEnvironmentMap implements ConvertFromPatch
{
    private static final Logger m_logger;
    static final DynamicElementDef[] DYNAMICS;
    static final ParticleDef[] PARTICLES;
    static final SoundDef[] SOUNDS;
    static final InteractiveElementDef[] INTERACTIVE_ELEMENT;
    @NotNull
    private AbstractBuildingStruct[] m_attachedBuilding;
    
    @Override
    public void fromPatch(final PartitionPatch patchTopLeft, final PartitionPatch patchTopRight, final PartitionPatch patchBottomLeft, final PartitionPatch patchBottomRight) {
        this.m_groundType = PartitionPatch.getMergedGroundType(patchTopLeft, patchTopRight, patchBottomLeft, patchBottomRight);
        final ClientPartitionPatch topLeft = (ClientPartitionPatch)patchTopLeft;
        final ClientPartitionPatch topRight = (ClientPartitionPatch)patchTopRight;
        final ClientPartitionPatch bottomLeft = (ClientPartitionPatch)patchBottomLeft;
        final ClientPartitionPatch bottomRight = (ClientPartitionPatch)patchBottomRight;
        this.setInteractiveElement(this.getMergedInteractiveElements(topLeft, topRight, bottomLeft, bottomRight));
        this.setSoundData(this.getMergedSounds(topLeft, topRight, bottomLeft, bottomRight));
        this.setParticleData(this.getMergedParticles(topLeft, topRight, bottomLeft, bottomRight));
        this.setDynamicElement(this.getMergedDynamicElements(topLeft, topRight, bottomLeft, bottomRight));
        this.setSeaPercent(topLeft, topRight, bottomLeft, bottomRight);
        this.setRiverDistribution(this.getMergedDistribution(topLeft, topRight, bottomLeft, bottomRight, EnvironmentData.SoundType.RIVER_INDEX));
        this.setLakeDistribution(this.getMergedDistribution(topLeft, topRight, bottomLeft, bottomRight, EnvironmentData.SoundType.LAKE_INDEX));
        this.setLavaDistribution(this.getMergedDistribution(topLeft, topRight, bottomLeft, bottomRight, EnvironmentData.SoundType.LAVA_INDEX));
        this.setRunningLavaDistribution(this.getMergedDistribution(topLeft, topRight, bottomLeft, bottomRight, EnvironmentData.SoundType.RUNNING_LAVA_INDEX));
    }
    
    private InteractiveElementDef[] getMergedInteractiveElements(final ClientPartitionPatch topLeft, final ClientPartitionPatch topRight, final ClientPartitionPatch bottomLeft, final ClientPartitionPatch bottomRight) {
        return WakfuClientEnvironmentMapPatch.INTERACTIVE_ELEMENT;
    }
    
    private DynamicElementDef[] getMergedDynamicElements(final ClientPartitionPatch topLeft, final ClientPartitionPatch topRight, final ClientPartitionPatch bottomLeft, final ClientPartitionPatch bottomRight) {
        final DynamicElementDef[] topLeftSD = (topLeft == null) ? WakfuClientEnvironmentMapPatch.DYNAMICS : topLeft.getEnvData().getDynamicElements();
        final DynamicElementDef[] topRightSD = (topRight == null) ? WakfuClientEnvironmentMapPatch.DYNAMICS : topRight.getEnvData().getDynamicElements();
        final DynamicElementDef[] bottomLeftSD = (bottomLeft == null) ? WakfuClientEnvironmentMapPatch.DYNAMICS : bottomLeft.getEnvData().getDynamicElements();
        final DynamicElementDef[] bottomRightSD = (bottomRight == null) ? WakfuClientEnvironmentMapPatch.DYNAMICS : bottomRight.getEnvData().getDynamicElements();
        final int count = topLeftSD.length + topRightSD.length + bottomLeftSD.length + bottomRightSD.length;
        if (count == 0) {
            return WakfuClientEnvironmentMapPatch.DYNAMICS;
        }
        final DynamicElementDef[] result = new DynamicElementDef[count];
        this.fill(result, topLeftSD, topRightSD, bottomLeftSD, bottomRightSD);
        return result;
    }
    
    private SoundDef[] getMergedSounds(final ClientPartitionPatch topLeft, final ClientPartitionPatch topRight, final ClientPartitionPatch bottomLeft, final ClientPartitionPatch bottomRight) {
        final SoundDef[] topLeftSD = (topLeft == null) ? WakfuClientEnvironmentMapPatch.SOUNDS : topLeft.getEnvData().getSoundData();
        final SoundDef[] topRightSD = (topRight == null) ? WakfuClientEnvironmentMapPatch.SOUNDS : topRight.getEnvData().getSoundData();
        final SoundDef[] bottomLeftSD = (bottomLeft == null) ? WakfuClientEnvironmentMapPatch.SOUNDS : bottomLeft.getEnvData().getSoundData();
        final SoundDef[] bottomRightSD = (bottomRight == null) ? WakfuClientEnvironmentMapPatch.SOUNDS : bottomRight.getEnvData().getSoundData();
        final int count = topLeftSD.length + topRightSD.length + bottomLeftSD.length + bottomRightSD.length;
        if (count == 0) {
            return WakfuClientEnvironmentMapPatch.SOUNDS;
        }
        final SoundDef[] result = new SoundDef[count];
        this.fill(result, topLeftSD, topRightSD, bottomLeftSD, bottomRightSD);
        return result;
    }
    
    private ParticleDef[] getMergedParticles(final ClientPartitionPatch topLeft, final ClientPartitionPatch topRight, final ClientPartitionPatch bottomLeft, final ClientPartitionPatch bottomRight) {
        final ParticleDef[] topLeftSD = (topLeft == null) ? WakfuClientEnvironmentMapPatch.PARTICLES : topLeft.getEnvData().getParticleData();
        final ParticleDef[] topRightSD = (topRight == null) ? WakfuClientEnvironmentMapPatch.PARTICLES : topRight.getEnvData().getParticleData();
        final ParticleDef[] bottomLeftSD = (bottomLeft == null) ? WakfuClientEnvironmentMapPatch.PARTICLES : bottomLeft.getEnvData().getParticleData();
        final ParticleDef[] bottomRightSD = (bottomRight == null) ? WakfuClientEnvironmentMapPatch.PARTICLES : bottomRight.getEnvData().getParticleData();
        final int count = topLeftSD.length + topRightSD.length + bottomLeftSD.length + bottomRightSD.length;
        if (count == 0) {
            return WakfuClientEnvironmentMapPatch.PARTICLES;
        }
        final ParticleDef[] result = new ParticleDef[count];
        this.fill(result, topLeftSD, topRightSD, bottomLeftSD, bottomRightSD);
        return result;
    }
    
    private void fill(final ElementDef[] result, final ElementDef[] topLeft, final ElementDef[] topRight, final ElementDef[] bottomLeft, final ElementDef[] bottomRight) {
        int idx = 0;
        for (int i = 0; i < topLeft.length; ++i) {
            final ElementDef duplicate;
            final ElementDef def = duplicate = topLeft[i].duplicate();
            duplicate.m_x += 0;
            final ElementDef elementDef = def;
            elementDef.m_y += 0;
            result[idx++] = def;
        }
        for (int i = 0; i < topRight.length; ++i) {
            final ElementDef duplicate2;
            final ElementDef def = duplicate2 = topRight[i].duplicate();
            duplicate2.m_x += 9;
            final ElementDef elementDef2 = def;
            elementDef2.m_y += 0;
            result[idx++] = def;
        }
        for (int i = 0; i < bottomLeft.length; ++i) {
            final ElementDef duplicate3;
            final ElementDef def = duplicate3 = bottomLeft[i].duplicate();
            duplicate3.m_x += 0;
            final ElementDef elementDef3 = def;
            elementDef3.m_y += 9;
            result[idx++] = def;
        }
        for (int i = 0; i < bottomRight.length; ++i) {
            final ElementDef duplicate4;
            final ElementDef def = duplicate4 = bottomRight[i].duplicate();
            duplicate4.m_x += 9;
            final ElementDef elementDef4 = def;
            elementDef4.m_y += 9;
            result[idx++] = def;
        }
    }
    
    @Override
    public void setAttachedBuilding(@NotNull final AbstractBuildingStruct[] attachedBuildings) {
        this.m_attachedBuilding = attachedBuildings;
    }
    
    private long getMergedDistribution(final ClientPartitionPatch topLeft, final ClientPartitionPatch topRight, final ClientPartitionPatch bottomLeft, final ClientPartitionPatch bottomRight, final EnvironmentData.SoundType soundType) {
        long distribution = 0L;
        final short topLeftSD = (short)((topLeft == null) ? 0 : topLeft.getEnvData().getSoundDistribution(soundType));
        final short topRightSD = (short)((topRight == null) ? 0 : topRight.getEnvData().getSoundDistribution(soundType));
        final short bottomLeftSD = (short)((bottomLeft == null) ? 0 : bottomLeft.getEnvData().getSoundDistribution(soundType));
        final short bottomRightSD = (short)((bottomRight == null) ? 0 : bottomRight.getEnvData().getSoundDistribution(soundType));
        for (int x = 0; x < 18; x += 3) {
            for (int y = 0; y < 18; y += 3) {
                final int index = EnvironmentConstants.computeDistributionIndex(x, y);
                final boolean value = this.getDistributionValue(x, y, topLeftSD, topRightSD, bottomLeftSD, bottomRightSD);
                distribution = MathHelper.setBooleanAt(distribution, index, value);
            }
        }
        return distribution;
    }
    
    private boolean getDistributionValue(final int x, final int y, final short topLeftSD, final short topRightSD, final short bottomLeftSD, final short bottomRightSD) {
        short array;
        if (x < 9) {
            array = ((y < 9) ? topLeftSD : bottomLeftSD);
        }
        else {
            array = ((y < 9) ? topRightSD : bottomRightSD);
        }
        final int px = x % 9;
        final int py = y % 9;
        final int index = EnvironmentConstants.computeDistributionIndex(px, py);
        return MathHelper.getBooleanAt(array, index);
    }
    
    private void setSeaPercent(final ClientPartitionPatch topLeft, final ClientPartitionPatch topRight, final ClientPartitionPatch bottomLeft, final ClientPartitionPatch bottomRight) {
        float seaPercent = 0.0f;
        seaPercent += ((topLeft != null) ? topLeft.getEnvData().getSeaPercent() : 0.0f);
        seaPercent += ((topRight != null) ? topRight.getEnvData().getSeaPercent() : 0.0f);
        seaPercent += ((bottomLeft != null) ? bottomLeft.getEnvData().getSeaPercent() : 0.0f);
        seaPercent += ((bottomRight != null) ? bottomRight.getEnvData().getSeaPercent() : 0.0f);
        this.setSeaPercent(seaPercent);
    }
    
    @Override
    public short getGroundType(final int cellX, final int cellY, final int cellZ) {
        if (this.m_attachedBuilding.length != 0) {
            for (final AbstractBuildingStruct building : this.m_attachedBuilding) {
                final int worldX = cellX + this.m_x * 18;
                final int worldY = cellY + this.m_y * 18;
                if (building.isSteryl(worldX, worldY, cellZ)) {
                    return 0;
                }
            }
        }
        return super.getGroundType(cellX, cellY, cellZ);
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuClientEnvironmentMapPatch.class);
        DYNAMICS = new DynamicElementDef[0];
        PARTICLES = new ParticleDef[0];
        SOUNDS = new SoundDef[0];
        INTERACTIVE_ELEMENT = new InteractiveElementDef[0];
    }
}
