package com.ankamagames.wakfu.client.core.game.fight.join;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fight.*;

class JoinAllyFightProcedure extends JoinFightProcedureBase
{
    private static final Logger m_logger;
    
    JoinAllyFightProcedure(@NotNull final FightInfo fight, @NotNull final LocalPlayerCharacter player, @NotNull final CharacterInfo joinedAlly) {
        super(fight, player);
        this.setJoinedAlly(joinedAlly);
    }
    
    @Override
    JoinFightResult tryJoinFightCore() {
        return this.joinAlly();
    }
    
    static {
        m_logger = Logger.getLogger((Class)JoinAllyFightProcedure.class);
    }
}
