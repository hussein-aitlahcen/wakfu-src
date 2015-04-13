package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightJoinMessageHandler extends UsingFightMessageHandler<FightJoinMessage, Fight>
{
    private static Logger m_logger;
    
    @Override
    public boolean onMessage(final FightJoinMessage msg) {
        final JoinFightAction action = new JoinFightAction(TimedAction.getNextUid(), FightActionType.JOIN_FIGHT.getId(), 0, ((Fight)this.m_concernedFight).getId());
        action.setTeamId(msg.getTeamId());
        action.setSerializedEffectUserDatas(msg.getSerializedEffectUserDatas());
        action.setSerializedFighterDatas(msg.getSerializedFighterDatas());
        action.setFighterId(msg.getFighterId());
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
        FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        return false;
    }
    
    private void reloadFighterForFight(final CharacterInfo fighter, final byte[] serializedFighterDatas, final byte[] serializedEffectuserDatas) {
        if (fighter == null) {
            return;
        }
        fighter.reloadCharacterForFight((Fight)this.m_concernedFight, serializedFighterDatas, serializedEffectuserDatas);
    }
    
    static {
        FightJoinMessageHandler.m_logger = Logger.getLogger((Class)FightJoinMessageHandler.class);
    }
}
