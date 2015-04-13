package com.ankamagames.wakfu.client.core.world.havenWorld;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;

public class EnvironmentData
{
    private float m_seaPercent;
    private short[] m_soundsDistribution;
    private final ClientEnvironmentMap m_mapData;
    
    public EnvironmentData() {
        super();
        this.m_mapData = new ClientEnvironmentMap();
    }
    
    public void read(final ExtendedDataInputStream stream) throws IOException {
        this.m_mapData.load(stream);
        this.m_seaPercent = stream.readFloat();
        this.m_soundsDistribution = stream.readShorts(4);
    }
    
    public float getSeaPercent() {
        return this.m_seaPercent;
    }
    
    short getSoundDistribution(final SoundType soundType) {
        return this.m_soundsDistribution[soundType.ordinal()];
    }
    
    public ParticleDef[] getParticleData() {
        final ParticleDef[] particleData = this.m_mapData.getParticleData();
        return (particleData == null) ? WakfuClientEnvironmentMapPatch.PARTICLES : particleData;
    }
    
    public SoundDef[] getSoundData() {
        final SoundDef[] soundData = this.m_mapData.getSoundData();
        return (soundData == null) ? WakfuClientEnvironmentMapPatch.SOUNDS : soundData;
    }
    
    public DynamicElementDef[] getDynamicElements() {
        final DynamicElementDef[] dynamicElements = this.m_mapData.getDynamicElements();
        return (dynamicElements == null) ? WakfuClientEnvironmentMapPatch.DYNAMICS : dynamicElements;
    }
    
    public enum SoundType
    {
        RIVER_INDEX, 
        LAKE_INDEX, 
        LAVA_INDEX, 
        RUNNING_LAVA_INDEX;
    }
}
