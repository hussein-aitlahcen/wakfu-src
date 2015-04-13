package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.world.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightCreationMessageHandler extends UsingFightMessageHandler<FightCreationMessage, Fight>
{
    private static Logger m_logger;
    
    @Override
    public boolean onMessage(final FightCreationMessage msg) {
        if (this.m_concernedFight != null) {
            FightCreationData.INSTANCE.m_fightCreation = true;
            FightCreationData.INSTANCE.m_creationActionSequenceOperations.setConcernedFight((Fight)this.m_concernedFight);
            WakfuGameEntity.getInstance().pushFrame(NetInFightManagementFrame.getInstance());
            final FightCreationAction creationAction = new FightCreationAction(TimedAction.getNextUid(), FightActionType.FIGHT_CREATION.getId(), 0, (Fight)this.m_concernedFight, msg, FightCreationData.INSTANCE.m_creationActionSequenceOperations);
            FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, creationAction);
            FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        }
        else {
            FightCreationMessageHandler.m_logger.error((Object)String.format("Impossible de crer le fight typeId=%d fightId=%d !", msg.getFightType(), msg.getFightId()));
        }
        return false;
    }
    
    static {
        FightCreationMessageHandler.m_logger = Logger.getLogger((Class)FightCreationMessageHandler.class);
    }
}
