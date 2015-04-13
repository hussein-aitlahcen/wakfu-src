package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class SimpleOccupationModificationMessageHandler extends UsingFightMessageHandler<SimpleOccupationModificationMessage, ExternalFightInfo>
{
    @Override
    public boolean onMessage(final SimpleOccupationModificationMessage msg) {
        final long concernedPlayerId = msg.getConcernedPlayerId();
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(concernedPlayerId);
        if (!character.isOnFight()) {
            return false;
        }
        FightActionGroupManager.getInstance().addActionToPendingGroup(character.getCurrentFightId(), new SimpleOccupationModificationAction(msg));
        FightActionGroupManager.getInstance().executePendingGroup(character.getCurrentFightId());
        return false;
    }
}
