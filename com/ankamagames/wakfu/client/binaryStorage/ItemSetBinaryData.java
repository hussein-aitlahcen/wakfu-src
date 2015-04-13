package com.ankamagames.wakfu.client.binaryStorage;

import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ItemSetBinaryData implements BinaryData
{
    protected short m_id;
    protected int m_linkedItemReferenceId;
    protected int[] m_itemsId;
    protected TIntObjectHashMap<int[]> m_effectIdsByPartCount;
    
    public short getId() {
        return this.m_id;
    }
    
    public int getLinkedItemReferenceId() {
        return this.m_linkedItemReferenceId;
    }
    
    public int[] getItemsId() {
        return this.m_itemsId;
    }
    
    public TIntObjectHashMap<int[]> getEffectIdsByPartCount() {
        return this.m_effectIdsByPartCount;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_linkedItemReferenceId = 0;
        this.m_itemsId = null;
        this.m_effectIdsByPartCount = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getShort();
        this.m_linkedItemReferenceId = buffer.getInt();
        this.m_itemsId = buffer.readIntArray();
        final int effectIdsByPartCountCount = buffer.getInt();
        this.m_effectIdsByPartCount = new TIntObjectHashMap<int[]>(effectIdsByPartCountCount);
        for (int iEffectIdsByPartCount = 0; iEffectIdsByPartCount < effectIdsByPartCountCount; ++iEffectIdsByPartCount) {
            final int effectIdsByPartCountKey = buffer.getInt();
            final int[] effectIdsByPartCountValue = buffer.readIntArray();
            this.m_effectIdsByPartCount.put(effectIdsByPartCountKey, effectIdsByPartCountValue);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.ITEM_SET.getId();
    }
}
