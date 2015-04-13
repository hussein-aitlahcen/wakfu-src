package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightJoinMessageHandler extends UsingFightMessageHandler<FightJoinMessage, ExternalFightInfo>
{
    @Override
    public boolean onMessage(final FightJoinMessage msg) {
        final FightInfo fight = FightManager.getInstance().getFightById(msg.getFightId());
        final boolean displayEffect = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight() == null;
        final CharacterInfo fighter = CharacterInfoManager.getInstance().getCharacter(msg.getFighterId());
        fighter.unserializeFighterDatas(msg.getSerializedFighterDatas());
        fighter.setFight(msg.getFightId());
        fighter.setOnFight(true);
        final ExternalFightInfo efi = (ExternalFightInfo)fight;
        efi.addFighter(fighter, msg.getTeamId());
        if (displayEffect) {
            fighter.getActor().addPassiveTeamParticleSystem(msg.getTeamId());
            if (fighter instanceof NonPlayerCharacter && (fight.getStatus() == AbstractFight.FightStatus.PLACEMENT || fight.getStatus() == AbstractFight.FightStatus.CREATION)) {
                fighter.getActor().addCrossSwordParticleSystem((byte)(-1));
            }
        }
        FightVisibilityManager.getInstance().onFighterJoinFight(fighter, msg.getFightId());
        fighter.changeToSpellAttackIfNecessary();
        return false;
    }
}
