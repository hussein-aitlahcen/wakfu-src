package com.ankamagames.framework.graphics.engine.particleSystem.definitions.attributesReaderWriter;

import com.ankamagames.framework.graphics.engine.particleSystem.definitions.*;
import com.ankamagames.framework.graphics.engine.particleSystem.attributesReaderWriter.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class EmitterDefinitionAttributesRW extends AffectorableAttributesRW<EmitterDefinition>
{
    public static final EmitterDefinitionAttributesRW m_instance;
    
    @Override
    protected byte getId() {
        return 1;
    }
    
    @Override
    protected boolean equals(final EmitterDefinition min, final EmitterDefinition max) {
        return min.m_maxParticlesCount == max.m_maxParticlesCount && min.m_maxParticlesPerSpawn == max.m_maxParticlesPerSpawn && min.m_spawnFrequency == max.m_spawnFrequency && min.m_particleLifeTime == max.m_particleLifeTime && min.m_spawnFrequencyRandom == max.m_spawnFrequencyRandom && min.m_particleLifeTimeRandom == max.m_particleLifeTimeRandom && min.m_particleOffsetX == max.m_particleOffsetX && min.m_particleOffsetY == max.m_particleOffsetY && min.m_particleOffsetZ == max.m_particleOffsetZ && min.m_particleOffsetRandomX == max.m_particleOffsetRandomX && min.m_particleOffsetRandomY == max.m_particleOffsetRandomY && min.m_particleOffsetRandomZ == max.m_particleOffsetRandomZ && min.m_particleVelocityX == max.m_particleVelocityX && min.m_particleVelocityY == max.m_particleVelocityY && min.m_particleVelocityZ == max.m_particleVelocityZ && min.m_particleVelocityRandomX == max.m_particleVelocityRandomX && min.m_particleVelocityRandomY == max.m_particleVelocityRandomY && min.m_particleVelocityRandomZ == max.m_particleVelocityRandomZ && min.m_startSpawnTime == max.m_startSpawnTime && min.m_endSpawnTime == max.m_endSpawnTime;
    }
    
    @Override
    public EmitterDefinition createFromStream(final ExtendedDataInputStream istream, final float levelPercent) throws IOException {
        final boolean leveled = istream.readBooleanBit();
        final boolean geocentric = istream.readBooleanBit();
        final int maxParticlesCount = AttributesReaderWriter.readUnsignedShort(istream, leveled, levelPercent);
        final int maxParticlesPerSpawn = AttributesReaderWriter.readUnsignedShort(istream, leveled, levelPercent);
        final float spawnFrequency = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleLifeTime = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float spawnFrequencyRandom = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleLifeTimeRandom = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleOffsetX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleOffsetY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleOffsetZ = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleOffsetRandomX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleOffsetRandomY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleOffsetRandomZ = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleVelocityX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleVelocityY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleVelocityZ = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleVelocityRandomX = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleVelocityRandomY = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float particleVelocityRandomZ = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float startSpawnTime = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        final float endSpawnTime = AttributesReaderWriter.readFloat(istream, leveled, levelPercent);
        return new EmitterDefinition(startSpawnTime, endSpawnTime, maxParticlesCount, maxParticlesPerSpawn, spawnFrequency, spawnFrequencyRandom, particleLifeTime, particleLifeTimeRandom, particleOffsetX, particleOffsetY, particleOffsetZ, particleOffsetRandomX, particleOffsetRandomY, particleOffsetRandomZ, particleVelocityX, particleVelocityY, particleVelocityZ, particleVelocityRandomX, particleVelocityRandomY, particleVelocityRandomZ, geocentric);
    }
    
    @Override
    protected void writeToStream(final OutputBitStream ostream, final EmitterDefinition min, final EmitterDefinition max) throws IOException {
        final boolean leveled = !this.equals(min, max);
        ostream.writeBooleanBit(leveled);
        ostream.writeBooleanBit(min.m_geocentric);
        ostream.align();
        AttributesReaderWriter.writeUnsignedShort(ostream, leveled, min.m_maxParticlesCount, max.m_maxParticlesCount);
        AttributesReaderWriter.writeUnsignedShort(ostream, leveled, min.m_maxParticlesPerSpawn, max.m_maxParticlesPerSpawn);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_spawnFrequency, max.m_spawnFrequency);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleLifeTime, max.m_particleLifeTime);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_spawnFrequencyRandom, max.m_spawnFrequencyRandom);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleLifeTimeRandom, max.m_particleLifeTimeRandom);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleOffsetX, max.m_particleOffsetX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleOffsetY, max.m_particleOffsetY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleOffsetZ, max.m_particleOffsetZ);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleOffsetRandomX, max.m_particleOffsetRandomX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleOffsetRandomY, max.m_particleOffsetRandomY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleOffsetRandomZ, max.m_particleOffsetRandomZ);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleVelocityX, max.m_particleVelocityX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleVelocityY, max.m_particleVelocityY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleVelocityZ, max.m_particleVelocityZ);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleVelocityRandomX, max.m_particleVelocityRandomX);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleVelocityRandomY, max.m_particleVelocityRandomY);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_particleVelocityRandomZ, max.m_particleVelocityRandomZ);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_startSpawnTime, max.m_startSpawnTime);
        AttributesReaderWriter.writeFloat(ostream, leveled, min.m_endSpawnTime, max.m_endSpawnTime);
    }
    
    static {
        m_instance = new EmitterDefinitionAttributesRW();
    }
}
