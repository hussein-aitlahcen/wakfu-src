package com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator;

import com.ankamagames.framework.external.*;

public enum CensorTypeEnum implements ExportableEnum
{
    OBSCENITY((short)0, "Filtre Obs\u00e9nit\u00e9"), 
    BANNISHED((short)1, "Banni"), 
    COPYRIGHTED((short)2, "Nom prot\u00e9g\u00e9");
    
    private final short m_id;
    private final String m_label;
    
    private CensorTypeEnum(final short id, final String label) {
        this.m_id = id;
        this.m_label = label;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public String getEnumId() {
        return Integer.toString(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static CensorTypeEnum getById(final short id) {
        for (final CensorTypeEnum b : values()) {
            if (b.getId() == id) {
                return b;
            }
        }
        return null;
    }
}
