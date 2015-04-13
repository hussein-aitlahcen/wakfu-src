package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterFledFromFightMessageHandler extends UsingFightMessageHandler<FighterFledFromFightMessage, Fight>
{
    private static Logger m_logger;
    
    @Override
    public boolean onMessage(final FighterFledFromFightMessage msg) {
        final int fightId = msg.getFightId();
        if (this.m_concernedFight == null || ((Fight)this.m_concernedFight).getId() != fightId) {
            FighterFledFromFightMessageHandler.m_logger.error((Object)("[_FL_] Frame invalide \u00e0 la r\u00e9ception d'un message " + FighterFledFromFightMessage.class.getSimpleName()));
            return false;
        }
        final Action removeController = new FighterFledFromFightAction(msg.getId(), FightActionType.FIGHTER_FLED.getId(), 0, fightId, msg.getFighterId());
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, removeController);
        FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        return false;
    }
    
    static {
        FighterFledFromFightMessageHandler.m_logger = Logger.getLogger((Class)FighterFledFromFightMessageHandler.class);
    }
}
