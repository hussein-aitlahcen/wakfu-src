package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ProtectorEcosystemProtectionBinaryData implements BinaryData
{
    protected int m_protectorId;
    protected ProtectorFaunaProtection[] m_faunaProtection;
    protected ProtectorFloraProtection[] m_floraProtection;
    
    public int getProtectorId() {
        return this.m_protectorId;
    }
    
    public ProtectorFaunaProtection[] getFaunaProtection() {
        return this.m_faunaProtection;
    }
    
    public ProtectorFloraProtection[] getFloraProtection() {
        return this.m_floraProtection;
    }
    
    @Override
    public void reset() {
        this.m_protectorId = 0;
        this.m_faunaProtection = null;
        this.m_floraProtection = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_protectorId = buffer.getInt();
        final int faunaProtectionCount = buffer.getInt();
        this.m_faunaProtection = new ProtectorFaunaProtection[faunaProtectionCount];
        for (int iFaunaProtection = 0; iFaunaProtection < faunaProtectionCount; ++iFaunaProtection) {
            (this.m_faunaProtection[iFaunaProtection] = new ProtectorFaunaProtection()).read(buffer);
        }
        final int floraProtectionCount = buffer.getInt();
        this.m_floraProtection = new ProtectorFloraProtection[floraProtectionCount];
        for (int iFloraProtection = 0; iFloraProtection < floraProtectionCount; ++iFloraProtection) {
            (this.m_floraProtection[iFloraProtection] = new ProtectorFloraProtection()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.PROTECTOR_ECOSYSTEM_PROTECTION.getId();
    }
    
    public static class ProtectorFaunaProtection
    {
        protected int m_monsterFamilyId;
        protected int m_protectionCost;
        protected int m_reintroductionCost;
        protected int m_reintroductionItemId;
        protected short m_reintroductionItemQty;
        
        public int getMonsterFamilyId() {
            return this.m_monsterFamilyId;
        }
        
        public int getProtectionCost() {
            return this.m_protectionCost;
        }
        
        public int getReintroductionCost() {
            return this.m_reintroductionCost;
        }
        
        public int getReintroductionItemId() {
            return this.m_reintroductionItemId;
        }
        
        public short getReintroductionItemQty() {
            return this.m_reintroductionItemQty;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_monsterFamilyId = buffer.getInt();
            this.m_protectionCost = buffer.getInt();
            this.m_reintroductionCost = buffer.getInt();
            this.m_reintroductionItemId = buffer.getInt();
            this.m_reintroductionItemQty = buffer.getShort();
        }
    }
    
    public static class ProtectorFloraProtection
    {
        protected int m_resourceFamilyId;
        protected int m_protectionCost;
        protected int m_reintroductionCost;
        protected int m_reintroductionItemId;
        protected short m_reintroductionItemQty;
        
        public int getResourceFamilyId() {
            return this.m_resourceFamilyId;
        }
        
        public int getProtectionCost() {
            return this.m_protectionCost;
        }
        
        public int getReintroductionCost() {
            return this.m_reintroductionCost;
        }
        
        public int getReintroductionItemId() {
            return this.m_reintroductionItemId;
        }
        
        public short getReintroductionItemQty() {
            return this.m_reintroductionItemQty;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_resourceFamilyId = buffer.getInt();
            this.m_protectionCost = buffer.getInt();
            this.m_reintroductionCost = buffer.getInt();
            this.m_reintroductionItemId = buffer.getInt();
            this.m_reintroductionItemQty = buffer.getShort();
        }
    }
}
