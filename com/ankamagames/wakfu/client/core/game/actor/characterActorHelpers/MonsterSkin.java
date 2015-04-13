package com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;

public class MonsterSkin
{
    private static final Logger m_logger;
    public final String m_monsterId;
    public final boolean m_displayEquipment;
    
    public MonsterSkin(final String monsterId, final boolean displayEquipment) {
        super();
        this.m_monsterId = monsterId;
        this.m_displayEquipment = displayEquipment;
    }
    
    public boolean equals(final MonsterSkin monsterSkin) {
        return this.m_displayEquipment == monsterSkin.m_displayEquipment && StringUtils.equals(this.m_monsterId, monsterSkin.m_monsterId, false);
    }
    
    public void applyTo(final CharacterInfo characterInfo) {
        final MonsterBreed breed = MonsterBreedManager.getInstance().getBreedFromId(PrimitiveConverter.getShort(this.m_monsterId));
        if (breed == null) {
            MonsterSkin.m_logger.error((Object)("breed inconnu " + this.m_monsterId));
            return;
        }
        MonsterSpecialGfxApplyer.applyCustoms(breed.getSpecialGfx(), characterInfo);
        MonsterSpecialGfxApplyer.applyColors(breed.getSpecialGfx(), characterInfo);
        MonsterSpecialGfxApplyer.applyDefaultAnims(breed.getSpecialGfx(), characterInfo);
    }
    
    static {
        m_logger = Logger.getLogger((Class)MonsterSkin.class);
    }
}
