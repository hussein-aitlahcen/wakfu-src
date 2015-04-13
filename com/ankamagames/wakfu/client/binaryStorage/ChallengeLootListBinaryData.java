package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ChallengeLootListBinaryData implements BinaryData
{
    protected int m_id;
    protected ChallengeLootEntry[] m_entries;
    
    public int getId() {
        return this.m_id;
    }
    
    public ChallengeLootEntry[] getEntries() {
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
        this.m_entries = new ChallengeLootEntry[entrieCount];
        for (int iEntrie = 0; iEntrie < entrieCount; ++iEntrie) {
            (this.m_entries[iEntrie] = new ChallengeLootEntry()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.CHALLENGE_LOOT_LIST.getId();
    }
    
    public static class ChallengeLootEntry
    {
        protected int m_challengeId;
        protected String m_criteria;
        
        public int getChallengeId() {
            return this.m_challengeId;
        }
        
        public String getCriteria() {
            return this.m_criteria;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_challengeId = buffer.getInt();
            this.m_criteria = buffer.readUTF8().intern();
        }
    }
}
