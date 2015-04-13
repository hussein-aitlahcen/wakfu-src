package com.ankamagames.wakfu.common.game.nation.handlers;

import com.ankamagames.wakfu.common.game.nation.*;

public final class NullHandlersFactory implements NationHandlersFactory
{
    private static NullHandlersFactory INSTANCE;
    
    public static NullHandlersFactory getInstance() {
        return NullHandlersFactory.INSTANCE;
    }
    
    @Override
    public NationMembersHandler createMembersHandler(final Nation nation) {
        return null;
    }
    
    @Override
    public NationPoliticHandler createPoliticHandler(final Nation nation) {
        return null;
    }
    
    @Override
    public NationBuffsHandler createBuffsHandler(final Nation nation) {
        return null;
    }
    
    @Override
    public NationJusticeHandler createJusticeHandler(final Nation nation) {
        return null;
    }
    
    static {
        NullHandlersFactory.INSTANCE = new NullHandlersFactory();
    }
}
