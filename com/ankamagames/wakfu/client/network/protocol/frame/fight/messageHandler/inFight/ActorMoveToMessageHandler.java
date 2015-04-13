package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class ActorMoveToMessageHandler extends UsingFightMessageHandler<ActorMoveToMessage, Fight>
{
    @Override
    public boolean onMessage(final ActorMoveToMessage msg) {
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(msg.getActorId());
        if (character == null) {
            return false;
        }
        if (!((Fight)this.m_concernedFight).containsFighter(character)) {
            return true;
        }
        final Action moveCharacterActorAction = new MoveCharacterActorAction(character.getId(), new Point3(msg.getX(), msg.getY(), msg.getZ()), Direction8.getDirectionFromIndex(msg.getDirection()));
        FightActionGroupManager.getInstance().addActionToPendingGroup((Fight)this.m_concernedFight, moveCharacterActorAction);
        FightActionGroupManager.getInstance().executePendingGroup(this.m_concernedFight);
        return false;
    }
}
