package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.graphics.engine.Anm2.Index.*;

class EquipementDef
{
    public final int m_crc;
    public final Anm m_anm;
    public final SpriteDefinition m_spriteDef;
    private CanHidePart m_canHidePart;
    public final int m_modification;
    
    EquipementDef(final int crc, final Anm anm, final SpriteDefinition spriteDef, final int modification) {
        super();
        this.m_canHidePart = null;
        this.m_crc = crc;
        this.m_anm = anm;
        this.m_spriteDef = spriteDef;
        this.m_modification = modification;
    }
    
    EquipementDef(final EquipementDef def) {
        super();
        this.m_canHidePart = null;
        this.m_crc = def.m_crc;
        this.m_anm = def.m_anm;
        this.m_spriteDef = null;
        this.m_modification = def.m_modification;
    }
    
    public final boolean canHide() {
        return this.m_anm.isReady() && this.m_anm.m_index.canHidePart();
    }
    
    public final CanHidePart getCanHidePart() {
        if (!this.canHide()) {
            return null;
        }
        if (this.m_canHidePart != null) {
            return this.m_canHidePart;
        }
        assert this.m_spriteDef != null;
        final String name = this.m_spriteDef.m_name;
        assert name != null;
        final CanHidePart[] parts = this.m_anm.m_index.getCanHideParts();
        for (int i = 0; i < parts.length; ++i) {
            if (name.contains(parts[i].itemName)) {
                this.m_canHidePart = parts[i];
                break;
            }
        }
        return this.m_canHidePart;
    }
}
