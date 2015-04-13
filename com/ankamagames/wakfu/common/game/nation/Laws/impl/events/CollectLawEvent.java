package com.ankamagames.wakfu.common.game.nation.Laws.impl.events;

import com.ankamagames.wakfu.common.game.craft.collect.*;
import com.ankamagames.wakfu.common.game.protector.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.resource.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;

public class CollectLawEvent extends NationLawEvent
{
    private final AbstractCollectAction m_collectAction;
    @Nullable
    private final AbstractProtectorEcosystemHandler m_protectorEcosystemHandler;
    private final Collectible m_collectible;
    
    public CollectLawEvent(final Citizen citizen, final Collectible collectible, final AbstractCollectAction collectAction, @Nullable final AbstractProtectorEcosystemHandler protectorEcosystemHandler) {
        super(citizen);
        this.m_collectible = collectible;
        this.m_collectAction = collectAction;
        this.m_protectorEcosystemHandler = protectorEcosystemHandler;
    }
    
    public Collectible getCollectible() {
        return this.m_collectible;
    }
    
    public AbstractCollectAction getCollectAction() {
        return this.m_collectAction;
    }
    
    @Nullable
    public AbstractProtectorEcosystemHandler getProtectorEcosystemHandler() {
        return this.m_protectorEcosystemHandler;
    }
    
    @Override
    public NationLawEventType getEventType() {
        return NationLawEventType.COLLECT;
    }
}
