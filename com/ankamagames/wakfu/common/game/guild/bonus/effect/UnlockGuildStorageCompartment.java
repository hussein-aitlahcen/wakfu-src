package com.ankamagames.wakfu.common.game.guild.bonus.effect;

import com.ankamagames.wakfu.common.game.guild.storage.*;

public class UnlockGuildStorageCompartment implements GuildBuffEffect
{
    private final GuildStorageCompartmentType m_type;
    
    public UnlockGuildStorageCompartment(final GuildStorageCompartmentType type) {
        super();
        this.m_type = type;
    }
    
    public GuildStorageCompartmentType getCompartmentType() {
        return this.m_type;
    }
    
    @Override
    public EffectTypeId getType() {
        return EffectTypeId.STORAGE_COMPARTMENT;
    }
}
