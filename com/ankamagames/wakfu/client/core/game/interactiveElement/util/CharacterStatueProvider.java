package com.ankamagames.wakfu.client.core.game.interactiveElement.util;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;

public abstract class CharacterStatueProvider
{
    private final CharacterStatue m_statue;
    private final String m_animName;
    private final TByteIntHashMap m_equipmentGfxIds;
    
    public CharacterStatueProvider(final CharacterStatue statue, final String animName) {
        super();
        this.m_equipmentGfxIds = new TByteIntHashMap();
        this.m_statue = statue;
        this.m_animName = animName;
    }
    
    public int putEquipmentGfxId(final byte equipmentPositionId, final int gfxId) {
        return this.m_equipmentGfxIds.put(equipmentPositionId, gfxId);
    }
    
    public void clearEquipments() {
        this.m_equipmentGfxIds.clear();
    }
    
    public CharacterStatue getStatue() {
        return this.m_statue;
    }
    
    public String getAnimName() {
        return this.m_animName;
    }
    
    public TByteIntHashMap getEquipmentGfxIds() {
        return this.m_equipmentGfxIds;
    }
    
    public abstract AbstractCharacterData getCharacterData();
    
    public abstract BinarSerialPart getSynchronizationPart();
}
