package com.ankamagames.wakfu.common.game.nation.handlers;

import com.ankamagames.wakfu.common.game.nation.*;

public interface NationHandlersFactory
{
    NationMembersHandler createMembersHandler(Nation p0);
    
    NationPoliticHandler createPoliticHandler(Nation p0);
    
    NationBuffsHandler createBuffsHandler(Nation p0);
    
    NationJusticeHandler createJusticeHandler(Nation p0);
}
