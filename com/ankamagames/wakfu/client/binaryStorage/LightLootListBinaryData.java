package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class LightLootListBinaryData implements BinaryData
{
    protected int m_id;
    protected LootEntry[] m_entries;
    
    public int getId() {
        return this.m_id;
    }
    
    public LootEntry[] getEntries() {
        return this.m_entries;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_entries = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        final int entrieCount = buffer.getInt();
        this.m_entries = new LootEntry[entrieCount];
        for (int iEntrie = 0; iEntrie < entrieCount; ++iEntrie) {
            (this.m_entries[iEntrie] = new LootEntry()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.LIGHT_LOOT_LIST.getId();
    }
    
    public static class LootEntry
    {
        protected int m_itemId;
        protected short m_quantity;
        protected short m_quantityPerItem;
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public short getQuantity() {
            return this.m_quantity;
        }
        
        public short getQuantityPerItem() {
            return this.m_quantityPerItem;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_itemId = buffer.getInt();
            this.m_quantity = buffer.getShort();
            this.m_quantityPerItem = buffer.getShort();
        }
    }
}
