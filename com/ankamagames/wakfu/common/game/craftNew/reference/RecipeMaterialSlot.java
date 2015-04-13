package com.ankamagames.wakfu.common.game.craftNew.reference;

import com.ankamagames.wakfu.common.game.craftNew.constant.*;
import java.util.*;
import com.ankamagames.framework.kernel.utils.*;

public class RecipeMaterialSlot
{
    private final MaterialType[] m_materialTypes;
    private final short m_minLevel;
    private final short m_minRarity;
    private final boolean m_optionnal;
    
    public RecipeMaterialSlot(final MaterialType[] materialTypes, final short minLevel, final short minRarity, final boolean optionnal) {
        super();
        this.m_materialTypes = materialTypes;
        this.m_minLevel = minLevel;
        this.m_minRarity = minRarity;
        this.m_optionnal = optionnal;
    }
    
    public MaterialType[] getMaterialTypes() {
        return this.m_materialTypes;
    }
    
    public short getMinLevel() {
        return this.m_minLevel;
    }
    
    public short getMinRarity() {
        return this.m_minRarity;
    }
    
    public boolean isOptionnal() {
        return this.m_optionnal;
    }
    
    @Override
    public String toString() {
        return "RecipeMaterialSlot{m_materialTypes=" + Arrays.toString(this.m_materialTypes) + ", m_minLevel=" + this.m_minLevel + ", m_minRarity=" + this.m_minRarity + ", m_optionnal=" + this.m_optionnal + '}';
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof RecipeMaterialSlot)) {
            return false;
        }
        final RecipeMaterialSlot rms = (RecipeMaterialSlot)obj;
        for (final MaterialType materialType : rms.getMaterialTypes()) {
            if (!ArrayUtils.contains(this.m_materialTypes, materialType)) {
                return false;
            }
        }
        return this.m_minLevel == rms.getMinLevel() && this.m_minRarity == rms.getMinLevel() && this.m_optionnal == rms.isOptionnal();
    }
}
