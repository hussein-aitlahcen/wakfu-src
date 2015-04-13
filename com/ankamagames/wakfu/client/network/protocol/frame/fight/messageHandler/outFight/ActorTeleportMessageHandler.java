package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.action.FightBeginning.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class ActorTeleportMessageHandler extends UsingFightMessageHandler<ActorTeleportMessage, FightInfo>
{
    @Override
    public boolean onMessage(final ActorTeleportMessage msg) {
        final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(msg.getActorId());
        if (info == null || info.getCurrentFightId() == -1) {
            return true;
        }
        final FightInfo fight = FightManager.getInstance().getFightById(info.getCurrentFightId());
        if (fight == null) {
            return false;
        }
        if (fight.getStatus() == AbstractFight.FightStatus.ACTION) {
            final CharacterTeleportAction action = CharacterTeleportAction.checkout(8, FightActionType.TELEPORT_CHARACTER.getId(), 0, info, new Point3(msg.getX(), msg.getY(), msg.getAltitude()), true);
            FightActionGroupManager.getInstance().addActionToPendingGroup(info.getCurrentFightId(), action);
            FightActionGroupManager.getInstance().executePendingGroup(fight);
        }
        else if (msg.isGenerateMove()) {
            info.getActor().moveTo(msg.getX(), msg.getY(), msg.getAltitude(), false, false);
        }
        return false;
    }
}
