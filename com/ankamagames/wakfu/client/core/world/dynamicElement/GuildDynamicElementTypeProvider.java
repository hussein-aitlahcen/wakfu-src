package com.ankamagames.wakfu.client.core.world.dynamicElement;

import com.ankamagames.wakfu.client.alea.graphics.*;
import com.ankamagames.baseImpl.graphics.game.DynamicElement.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;

abstract class GuildDynamicElementTypeProvider extends WakfuDynamicElementTypeProvider
{
    private AnmGuildBlazonApplicator m_guildBlasonApplicator;
    
    @Override
    public void initialize(final DynamicElement elt) {
        this.m_guildBlasonApplicator = AnmGuildBlazonApplicator.create(elt, this.getGuildBlazon(), "blason", "blason_bg");
    }
    
    @Override
    public void clear(final DynamicElement elt) {
        if (this.m_guildBlasonApplicator != null) {
            this.m_guildBlasonApplicator.removeGuildAppearance();
            this.m_guildBlasonApplicator = null;
        }
    }
    
    protected abstract long getGuildBlazon();
}
