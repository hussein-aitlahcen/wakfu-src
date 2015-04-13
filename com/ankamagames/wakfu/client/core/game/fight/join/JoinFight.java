package com.ankamagames.wakfu.client.core.game.fight.join;

import com.ankamagames.wakfu.client.core.game.fight.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.fight.*;

public class JoinFight
{
    public static JoinFightProcedure joinAlly(@NotNull final FightInfo fight, @NotNull final CharacterInfo joinedAlly) {
        return new JoinAllyFightProcedure(fight, WakfuGameEntity.getInstance().getLocalPlayer(), joinedAlly);
    }
    
    public static JoinFightProcedure joinGroupMember(@NotNull final FightInfo fight, @NotNull final CharacterInfo joinedGroupMember) {
        return new JoinGroupFightProcedure(fight, WakfuGameEntity.getInstance().getLocalPlayer(), joinedGroupMember);
    }
    
    public static JoinFightProcedure autoJoin(@NotNull final FightInfo fight) {
        return new AutoJoinFightProcedure(fight, WakfuGameEntity.getInstance().getLocalPlayer());
    }
    
    public static JoinFightProcedure resumeProtectorAssault(@NotNull final Protector protector) {
        final FightInfo fight = protector.getNpc().getCurrentExternalFightInfo();
        if (fight == null) {
            return new JoinFightProcedure() {
                @Override
                public JoinFightResult tryJoinFight() {
                    return JoinFightResult.TARGET_NOT_IN_FIGHT;
                }
                
                @Override
                public JoinFightResult canJoinFight() {
                    return JoinFightResult.TARGET_NOT_IN_FIGHT;
                }
                
                @Override
                public void suppressQueries() {
                }
                
                @Override
                public boolean isJoinProtectorAttack() {
                    return false;
                }
            };
        }
        return new AutoJoinFightProcedure(fight, WakfuGameEntity.getInstance().getLocalPlayer()).withoutVisibilityCheck();
    }
}
