package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.FightBeginning.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class ActorTeleportMessageHandler extends UsingFightMessageHandler<ActorTeleportMessage, Fight>
{
    private static final Logger m_logger;
    
    @Override
    public boolean onMessage(final ActorTeleportMessage msg) {
        final CharacterInfo info = ((Fight)this.m_concernedFight).getFighterFromId(msg.getActorId());
        if (info == null) {
            ActorTeleportMessageHandler.m_logger.error((Object)("Impossible de t\u00e9l\u00e9porter un personnage qui n'est pas dans le combat " + msg.getActorId()));
            return true;
        }
        if (FightCreationData.INSTANCE.m_fightCreation) {
            FightCreationData.INSTANCE.m_creationActionSequenceOperations.addCharacterTeleport(info, new Point3(msg.getX(), msg.getY(), msg.getAltitude()));
            return false;
        }
        if (FightCreationData.INSTANCE.m_fightIsGonnaFinish || ((Fight)this.m_concernedFight).getStatus() == AbstractFight.FightStatus.PLACEMENT || ((Fight)this.m_concernedFight).getStatus() == AbstractFight.FightStatus.CREATION) {
            final CharacterTeleportAction action = CharacterTeleportAction.checkout(TimedAction.getNextUid(), FightActionType.TELEPORT_CHARACTER.getId(), 0, info, new Point3(msg.getX(), msg.getY(), msg.getAltitude()), msg.isGenerateMove());
            FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, action);
            FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
            return false;
        }
        ActorTeleportMessageHandler.m_logger.error((Object)("Impossible de t\u00e9l\u00e9porter un personnage du combat " + msg.getActorId()));
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ActorTeleportMessageHandler.class);
    }
}
