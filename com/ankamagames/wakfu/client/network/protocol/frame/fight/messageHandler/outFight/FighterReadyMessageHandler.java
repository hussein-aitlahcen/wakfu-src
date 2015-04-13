package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterReadyMessageHandler extends UsingFightMessageHandler<FighterReadyMessage, FightInfo>
{
    @Override
    public boolean onMessage(final FighterReadyMessage msg) {
        final FightInfo fight = FightManager.getInstance().getFightById(msg.getFightId());
        final CharacterInfo fighter = fight.getFighterFromId(msg.getFighterId());
        if (fighter == null) {
            return false;
        }
        final CharacterActor actor = fighter.getActor();
        if (msg.isReady()) {
            WeaponAnimHelper.startUsage(actor, actor.getCurrentAttack());
        }
        else {
            actor.setStaticAnimationKey("AnimStatique");
            actor.setAnimation("AnimStatique");
        }
        return false;
    }
}
