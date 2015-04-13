package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class MagiCraftLootListBinaryData implements BinaryData
{
    protected int m_id;
    protected byte m_gemType;
    protected LootEntry[] m_entries;
    
    public int getId() {
        return this.m_id;
    }
    
    public byte getGemType() {
        return this.m_gemType;
    }
    
    public LootEntry[] getEntries() {
        return this.m_entries;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_gemType = 0;
        this.m_entries = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_gemType = buffer.get();
        final int entrieCount = buffer.getInt();
        this.m_entries = new LootEntry[entrieCount];
        for (int iEntrie = 0; iEntrie < entrieCount; ++iEntrie) {
            (this.m_entries[iEntrie] = new LootEntry()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.MAGICRAFT_LOOT_LIST.getId();
    }
    
    public static class LootEntry
    {
        protected int m_itemId;
        protected double m_dropRate;
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public double getDropRate() {
            return this.m_dropRate;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_itemId = buffer.getInt();
            this.m_dropRate = buffer.getDouble();
        }
    }
}
