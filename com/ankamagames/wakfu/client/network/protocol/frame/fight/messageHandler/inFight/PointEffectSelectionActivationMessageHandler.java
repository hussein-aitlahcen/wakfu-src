package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class PointEffectSelectionActivationMessageHandler extends UsingFightMessageHandler<PointEffectSelectionActivationMessage, Fight>
{
    private static final Logger m_logger;
    
    @Override
    public boolean onMessage(final PointEffectSelectionActivationMessage msg) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Fight fight = localPlayer.getCurrentFight();
        if (fight == null) {
            PointEffectSelectionActivationMessageHandler.m_logger.warn((Object)"on re\u00e7oit un message de s\u00e9lection de pointEffect hors combat !?");
            return false;
        }
        final CharacterInfo fighter = fight.getTimeline().getFighter(msg.getFighterId());
        final PointEffectSelectionActivationAction action = new PointEffectSelectionActivationAction(TimedAction.getNextUid(), 0, FightActionType.POINT_EFFECT_SELECTION_ACTIVATION.getId(), fighter, msg.getRemainingMillis());
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
        FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PointEffectSelectionActivationMessageHandler.class);
    }
}
