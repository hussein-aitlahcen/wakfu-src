package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.FightBeginning.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class ActorPathUpdateMessageHandler extends UsingFightMessageHandler<ActorPathUpdateMessage, ExternalFightInfo>
{
    @Override
    public boolean onMessage(final ActorPathUpdateMessage msg) {
        final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(msg.getActorId());
        if (info != null && info.getCurrentFightId() != -1 && ExternalFightCreationActions.INSTANCE.m_fightIsInFightCreation.contains(info.getCurrentFightId())) {
            if (ExternalFightCreationActions.INSTANCE.m_characterMoveWithEndedListenerAction.get(info.getCurrentFightId()) == null) {
                ExternalFightCreationActions.INSTANCE.m_characterMoveWithEndedListenerAction.put(info.getCurrentFightId(), CharacterMoveWithEndedListenerAction.checkout(7, FightActionType.MOVE_CHARACTER.getId(), 0, info.getActor(), msg.getPath()));
            }
            else {
                ExternalFightCreationActions.INSTANCE.m_characterMoveWithEndedListenerAction.get(info.getCurrentFightId()).addCharacterMovement(info.getActor(), msg.getPath());
            }
            return false;
        }
        return true;
    }
}
