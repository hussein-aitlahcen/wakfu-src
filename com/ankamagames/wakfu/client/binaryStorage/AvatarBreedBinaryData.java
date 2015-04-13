package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class AvatarBreedBinaryData implements BinaryData
{
    protected int m_id;
    protected String m_name;
    protected int m_backgroundAps;
    protected int m_baseHp;
    protected int m_baseAp;
    protected int m_baseMp;
    protected int m_baseWp;
    protected int m_baseInit;
    protected int m_baseFerocity;
    protected int m_baseFumble;
    protected int m_baseWisdom;
    protected int m_baseTackle;
    protected int m_baseDodge;
    protected int m_baseProspection;
    protected int m_timerCountBeforeDeath;
    protected int m_preferedArea;
    protected byte[] m_spellElements;
    protected float[] m_characRatios;
    
    public int getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public int getBackgroundAps() {
        return this.m_backgroundAps;
    }
    
    public int getBaseHp() {
        return this.m_baseHp;
    }
    
    public int getBaseAp() {
        return this.m_baseAp;
    }
    
    public int getBaseMp() {
        return this.m_baseMp;
    }
    
    public int getBaseWp() {
        return this.m_baseWp;
    }
    
    public int getBaseInit() {
        return this.m_baseInit;
    }
    
    public int getBaseFerocity() {
        return this.m_baseFerocity;
    }
    
    public int getBaseFumble() {
        return this.m_baseFumble;
    }
    
    public int getBaseWisdom() {
        return this.m_baseWisdom;
    }
    
    public int getBaseTackle() {
        return this.m_baseTackle;
    }
    
    public int getBaseDodge() {
        return this.m_baseDodge;
    }
    
    public int getBaseProspection() {
        return this.m_baseProspection;
    }
    
    public int getTimerCountBeforeDeath() {
        return this.m_timerCountBeforeDeath;
    }
    
    public int getPreferedArea() {
        return this.m_preferedArea;
    }
    
    public byte[] getSpellElements() {
        return this.m_spellElements;
    }
    
    public float[] getCharacRatios() {
        return this.m_characRatios;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_name = null;
        this.m_backgroundAps = 0;
        this.m_baseHp = 0;
        this.m_baseAp = 0;
        this.m_baseMp = 0;
        this.m_baseWp = 0;
        this.m_baseInit = 0;
        this.m_baseFerocity = 0;
        this.m_baseFumble = 0;
        this.m_baseWisdom = 0;
        this.m_baseTackle = 0;
        this.m_baseDodge = 0;
        this.m_baseProspection = 0;
        this.m_timerCountBeforeDeath = 0;
        this.m_preferedArea = 0;
        this.m_spellElements = null;
        this.m_characRatios = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_name = buffer.readUTF8().intern();
        this.m_backgroundAps = buffer.getInt();
        this.m_baseHp = buffer.getInt();
        this.m_baseAp = buffer.getInt();
        this.m_baseMp = buffer.getInt();
        this.m_baseWp = buffer.getInt();
        this.m_baseInit = buffer.getInt();
        this.m_baseFerocity = buffer.getInt();
        this.m_baseFumble = buffer.getInt();
        this.m_baseWisdom = buffer.getInt();
        this.m_baseTackle = buffer.getInt();
        this.m_baseDodge = buffer.getInt();
        this.m_baseProspection = buffer.getInt();
        this.m_timerCountBeforeDeath = buffer.getInt();
        this.m_preferedArea = buffer.getInt();
        this.m_spellElements = buffer.readByteArray();
        this.m_characRatios = buffer.readFloatArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.AVATAR_BREED.getId();
    }
}
