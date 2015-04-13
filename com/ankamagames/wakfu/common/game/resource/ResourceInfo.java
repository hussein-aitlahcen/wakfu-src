package com.ankamagames.wakfu.common.game.resource;

public final class ResourceInfo
{
    private short m_refId;
    private int m_resourceX;
    private int m_resourceY;
    private short m_resourceZ;
    private byte m_evolutionStep;
    private boolean m_autoRespawn;
    private boolean m_justGrowth;
    
    public ResourceInfo(final short refId) {
        super();
        this.m_refId = refId;
    }
    
    public ResourceInfo(final short refId, final int resourceX, final int resourceY, final short resourceZ, final byte evolutionStep, final boolean autoRespawn, final boolean justGrowth) {
        super();
        this.m_refId = refId;
        this.m_resourceX = resourceX;
        this.m_resourceY = resourceY;
        this.m_resourceZ = resourceZ;
        this.m_evolutionStep = evolutionStep;
        this.m_autoRespawn = autoRespawn;
        this.m_justGrowth = justGrowth;
    }
    
    public short getRefId() {
        return this.m_refId;
    }
    
    public int getResourceX() {
        return this.m_resourceX;
    }
    
    public int getResourceY() {
        return this.m_resourceY;
    }
    
    public short getResourceZ() {
        return this.m_resourceZ;
    }
    
    public byte getEvolutionStep() {
        return this.m_evolutionStep;
    }
    
    public boolean isAutoRespawn() {
        return this.m_autoRespawn;
    }
    
    public boolean isJustGrowth() {
        return this.m_justGrowth;
    }
    
    int serialize(final short partitionX, final short partitionY) {
        final int relativeX = this.m_resourceX - partitionX * 18;
        final int relativeY = this.m_resourceY - partitionY * 18;
        if (relativeX < 0 || relativeX >= 18) {
            throw new RuntimeException("relativeX invalide: " + relativeX);
        }
        if (relativeY < 0 || relativeY >= 18) {
            throw new RuntimeException("relativeY invalide: " + relativeY);
        }
        final int relativeXY = relativeX * 18 + relativeY;
        assert (relativeXY & 0x1FF) == relativeXY : "relativeXY invalide: " + relativeXY;
        final int z = this.m_resourceZ & 0xFFFF;
        final int step = this.m_evolutionStep & 0x1F;
        if ((this.m_evolutionStep & 0x1F) != this.m_evolutionStep) {
            throw new RuntimeException("EvolutionStep trop grand: " + this.m_evolutionStep);
        }
        final int autoRespawnValue = this.m_autoRespawn ? 1 : 0;
        final int justSpawnValue = this.m_justGrowth ? 1 : 0;
        return justSpawnValue << 31 | autoRespawnValue << 30 | step << 25 | z << 9 | relativeXY;
    }
    
    void unserialize(final int serialized, final short partitionX, final short partitionY) {
        final int relativeXY = serialized & 0x1FF;
        this.m_resourceX = partitionX * 18 + relativeXY / 18;
        this.m_resourceY = partitionY * 18 + relativeXY % 18;
        this.m_resourceZ = (short)(serialized >>> 9 & 0xFFFF);
        this.m_evolutionStep = (byte)(serialized >>> 25 & 0x1F);
        this.m_autoRespawn = ((serialized >>> 30 & 0x1) == 0x1);
        this.m_justGrowth = (serialized >>> 31 == 1);
    }
}
