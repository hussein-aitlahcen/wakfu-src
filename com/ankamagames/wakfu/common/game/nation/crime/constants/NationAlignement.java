package com.ankamagames.wakfu.common.game.nation.crime.constants;

import com.ankamagames.framework.external.*;
import org.jetbrains.annotations.*;

public enum NationAlignement implements ExportableEnum
{
    ENEMY(-1, "Nation ennemie"), 
    ALLIED(1, "Nation alli\u00e9e");
    
    private final byte m_id;
    private final String m_label;
    
    private NationAlignement(final int id, final String label) {
        this.m_id = (byte)id;
        this.m_label = label;
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
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    @Nullable
    public static NationAlignement getFromId(final byte alignementId) {
        final NationAlignement[] values = values();
        for (int i = 0, length = values.length; i < length; ++i) {
            final NationAlignement nationAlignement = values[i];
            if (nationAlignement.m_id == alignementId) {
                return nationAlignement;
            }
        }
        return null;
    }
}
