package com.ankamagames.wakfu.client.core.game.fight.join;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.fight.*;

class AutoJoinFightProcedure extends JoinFightProcedureBase
{
    private static final Logger m_logger;
    private boolean m_checkVisibility;
    
    AutoJoinFightProcedure(@NotNull final FightInfo fight, @NotNull final LocalPlayerCharacter player) {
        super(fight, player);
        this.m_checkVisibility = true;
    }
    
    AutoJoinFightProcedure withoutVisibilityCheck() {
        this.m_checkVisibility = false;
        return this;
    }
    
    @Override
    JoinFightResult tryJoinFightCore() {
        return this.autoJoinFight();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AutoJoinFightProcedure.class);
    }
}
