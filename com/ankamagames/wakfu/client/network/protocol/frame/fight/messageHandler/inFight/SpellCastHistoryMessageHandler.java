package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class SpellCastHistoryMessageHandler extends UsingFightMessageHandler<SpellCastHistoryMessage, Fight>
{
    @Override
    public boolean onMessage(final SpellCastHistoryMessage msg) {
        final CharacterInfo fighter = ((Fight)this.m_concernedFight).getFighterFromId(msg.getCharacterId());
        if (fighter == null) {
            return false;
        }
        fighter.getSpellLevelCastHistory().unserialize(ByteBuffer.wrap(msg.getSerializedSpellCastHistory()));
        return false;
    }
}
