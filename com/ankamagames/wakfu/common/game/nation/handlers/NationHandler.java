package com.ankamagames.wakfu.common.game.nation.handlers;

import com.ankamagames.wakfu.common.game.nation.event.*;
import com.ankamagames.wakfu.common.game.nation.*;

public abstract class NationHandler<E extends NationEventHandler>
{
    private final Nation m_nation;
    
    protected NationHandler(final Nation nation) {
        super();
        this.m_nation = nation;
    }
    
    public Nation getNation() {
        return this.m_nation;
    }
    
    public abstract void finishInitialization();
    
    public abstract void registerEventHandler(final E p0);
    
    public abstract void unregisterEventHandler(final E p0);
}
