package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class AptitudeBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_breedId;
    protected byte m_characteristicId;
    protected int m_uiId;
    protected int m_aptitudeGfxId;
    protected int m_spellGfxId;
    protected short m_maxLevel;
    protected int m_constantCost;
    protected int[] m_variableCost;
    protected int m_linkedSpellId;
    protected int[] m_levelUnlock;
    protected int[] m_effectIds;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getBreedId() {
        return this.m_breedId;
    }
    
    public byte getCharacteristicId() {
        return this.m_characteristicId;
    }
    
    public int getUiId() {
        return this.m_uiId;
    }
    
    public int getAptitudeGfxId() {
        return this.m_aptitudeGfxId;
    }
    
    public int getSpellGfxId() {
        return this.m_spellGfxId;
    }
    
    public short getMaxLevel() {
        return this.m_maxLevel;
    }
    
    public int getConstantCost() {
        return this.m_constantCost;
    }
    
    public int[] getVariableCost() {
        return this.m_variableCost;
    }
    
    public int getLinkedSpellId() {
        return this.m_linkedSpellId;
    }
    
    public int[] getLevelUnlock() {
        return this.m_levelUnlock;
    }
    
    public int[] getEffectIds() {
        return this.m_effectIds;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_breedId = 0;
        this.m_characteristicId = 0;
        this.m_uiId = 0;
        this.m_aptitudeGfxId = 0;
        this.m_spellGfxId = 0;
        this.m_maxLevel = 0;
        this.m_constantCost = 0;
        this.m_variableCost = null;
        this.m_linkedSpellId = 0;
        this.m_levelUnlock = null;
        this.m_effectIds = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_breedId = buffer.getShort();
        this.m_characteristicId = buffer.get();
        this.m_uiId = buffer.getInt();
        this.m_aptitudeGfxId = buffer.getInt();
        this.m_spellGfxId = buffer.getInt();
        this.m_maxLevel = buffer.getShort();
        this.m_constantCost = buffer.getInt();
        this.m_variableCost = buffer.readIntArray();
        this.m_linkedSpellId = buffer.getInt();
        this.m_levelUnlock = buffer.readIntArray();
        this.m_effectIds = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.APTITUDE.getId();
    }
}
