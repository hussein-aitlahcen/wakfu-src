package com.ankamagames.wakfu.common.game.protector;

import gnu.trove.*;

public enum ProtectorMerchantItemType
{
    CHALLENGE((byte)1, 5055), 
    BUFF((byte)2, 5066), 
    CLIMATE_BUFF((byte)3, 5071);
    
    private static final TByteObjectHashMap<ProtectorMerchantItemType> m_typesById;
    private byte m_typeId;
    private int m_fakeReferenceId;
    
    private ProtectorMerchantItemType(final byte typeId, final int fakeReferenceId) {
        this.m_typeId = typeId;
        this.m_fakeReferenceId = fakeReferenceId;
    }
    
    public byte getTypeId() {
        return this.m_typeId;
    }
    
    public int getFakeReferenceId() {
        return this.m_fakeReferenceId;
    }
    
    public static ProtectorMerchantItemType getById(final byte id) {
        return ProtectorMerchantItemType.m_typesById.get(id);
    }
    
    static {
        m_typesById = new TByteObjectHashMap<ProtectorMerchantItemType>();
        for (final ProtectorMerchantItemType type : values()) {
            ProtectorMerchantItemType.m_typesById.put(type.getTypeId(), type);
        }
    }
}
