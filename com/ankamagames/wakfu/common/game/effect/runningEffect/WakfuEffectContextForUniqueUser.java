package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public class WakfuEffectContextForUniqueUser extends EffectContextForUniqueEffectUser<WakfuEffect>
{
    private final byte m_type;
    
    public WakfuEffectContextForUniqueUser(final EffectUser effectUser, final byte type) {
        super(effectUser);
        this.m_type = type;
    }
    
    @Override
    public FightMap getFightMap() {
        return null;
    }
    
    @Override
    public SpellCaster getSpellCaster() {
        return null;
    }
    
    @Override
    public MonsterSpawner getMonsterSpawner() {
        return null;
    }
    
    @Override
    public byte getContextType() {
        return this.m_type;
    }
}
