package com.ankamagames.wakfu.common.datas;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.fighter.*;

public final class SpellCostModification
{
    private TObjectIntHashMap<SpellPropertyType> m_apModifications;
    private TObjectIntHashMap<SpellPropertyType> m_rangeMaxModifications;
    
    public void apCostModification(final int value, final SpellPropertyType propertyType) {
        if (this.m_apModifications == null) {
            this.m_apModifications = new TObjectIntHashMap<SpellPropertyType>();
        }
        this.m_apModifications.adjustOrPutValue(propertyType, value, value);
    }
    
    public void rangeMaxModification(final int value, final SpellPropertyType propertyType) {
        if (this.m_rangeMaxModifications == null) {
            this.m_rangeMaxModifications = new TObjectIntHashMap<SpellPropertyType>();
        }
        this.m_rangeMaxModifications.adjustOrPutValue(propertyType, value, value);
    }
    
    public int getApCostModification(final SpellPropertyType propertyType) {
        if (this.m_apModifications == null) {
            return 0;
        }
        return this.m_apModifications.get(propertyType);
    }
    
    public int getRangeMaxModification(final SpellPropertyType propertyType) {
        if (this.m_rangeMaxModifications == null) {
            return 0;
        }
        return this.m_rangeMaxModifications.get(propertyType);
    }
    
    public void clear() {
        this.m_apModifications = null;
        this.m_rangeMaxModifications = null;
    }
    
    public int getModification(final FighterCharacteristicType type, final SpellPropertyType spellPropertyType) {
        if (type == FighterCharacteristicType.AP) {
            return this.getApCostModification(spellPropertyType);
        }
        if (type == FighterCharacteristicType.RANGE) {
            return this.getRangeMaxModification(spellPropertyType);
        }
        return 0;
    }
}
