package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterReadyMessageHandler extends UsingFightMessageHandler<FighterReadyMessage, Fight>
{
    @Override
    public boolean onMessage(final FighterReadyMessage msg) {
        final CharacterInfo fighter = ((Fight)this.m_concernedFight).getFighterFromId(msg.getFighterId());
        if (fighter == null || ((Fight)this.m_concernedFight).getStatus() != AbstractFight.FightStatus.PLACEMENT) {
            return false;
        }
        final CharacterActor actor = fighter.getActor();
        if (msg.isReady()) {
            if (fighter instanceof PlayerCharacter) {
                actor.setReadyForFight();
            }
            ((Fight)this.m_concernedFight).addFighterReady(fighter);
        }
        else {
            if (fighter instanceof PlayerCharacter) {
                actor.unSetReadyForFight();
            }
            ((Fight)this.m_concernedFight).removeFighterReady(fighter);
        }
        return false;
    }
}
