package com.ankamagames.wakfu.client.core.game.fight.join;

import com.ankamagames.wakfu.common.game.fight.*;

public interface JoinFightProcedure
{
    JoinFightResult tryJoinFight();
    
    JoinFightResult canJoinFight();
    
    void suppressQueries();
    
    boolean isJoinProtectorAttack();
}
