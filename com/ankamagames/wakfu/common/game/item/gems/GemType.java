package com.ankamagames.wakfu.common.game.item.gems;

import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.byteKey.*;

public enum GemType implements ExportableEnum
{
    NONE((byte)0), 
    ATTACK((byte)1), 
    DEFENSE((byte)2), 
    SUPPORT((byte)3);
    
    private static final ByteObjectLightWeightMap<GemType> GEM_TYPES_BY_ID;
    private final byte m_id;
    
    private GemType(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Override
    public String getEnumComment() {
        return this.toString();
    }
    
    public static GemType getById(final byte gemTypeId) {
        return GemType.GEM_TYPES_BY_ID.get(gemTypeId);
    }
    
    static {
        GEM_TYPES_BY_ID = new ByteObjectLightWeightMap<GemType>();
        for (final GemType type : values()) {
            GemType.GEM_TYPES_BY_ID.put(type.m_id, type);
        }
    }
}
