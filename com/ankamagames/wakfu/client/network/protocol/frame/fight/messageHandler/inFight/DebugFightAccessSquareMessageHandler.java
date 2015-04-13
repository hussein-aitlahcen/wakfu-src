package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class DebugFightAccessSquareMessageHandler extends UsingFightMessageHandler<DebugFightAccessSquareMessage, Fight>
{
    @Override
    public boolean onMessage(final DebugFightAccessSquareMessage msg) {
        final CharacterInfo selectedCharacter = CharacterInfoManager.getInstance().getCharacter(msg.getCharacterId());
        selectedCharacter.setDebugFightMessage(msg);
        DebugDisplayZone.getInstance().setInfos(msg.getNbCells(), msg.getCoordinates());
        return false;
    }
}
