package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

final class FightPlacementEndMessageHandler extends UsingFightMessageHandler<FightPlacementEndMessage, ExternalFightInfo>
{
    @Override
    public boolean onMessage(final FightPlacementEndMessage msg) {
        FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), new PlacementEndAction(TimedAction.getNextUid(), FightActionType.END_PLACEMENT.getId(), 0, msg.getFightId()));
        return false;
    }
    
    private static class PlacementEndAction extends AbstractFightAction
    {
        protected PlacementEndAction(final int uniqueId, final int actionType, final int actionId, final int fightId) {
            super(uniqueId, actionType, actionId, fightId);
        }
        
        @Override
        protected void runCore() {
            final FightInfo fight = this.getFight();
            if (fight == null) {
                return;
            }
            ((ExternalFightInfo)fight).setStatus(AbstractFight.FightStatus.ACTION);
            for (final CharacterInfo fighter : fight.getFighters()) {
                if (fighter instanceof PlayerCharacter) {
                    final CharacterActor actor = fighter.getActor();
                    WeaponAnimHelper.startUsage(actor, actor.getCurrentAttack());
                }
                this.cleanAllReadyForFightIcons(fight);
            }
        }
        
        private void cleanAllReadyForFightIcons(final FightInfo fight) {
            for (final CharacterInfo characterInfo : fight.getFighters()) {
                characterInfo.getActor().clearCrossSwordParticleSystem();
            }
        }
        
        @Override
        protected void onActionFinished() {
        }
    }
}
