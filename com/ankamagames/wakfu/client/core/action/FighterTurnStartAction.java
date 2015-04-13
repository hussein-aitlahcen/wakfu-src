package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public class FighterTurnStartAction extends AbstractFightAction
{
    public FighterTurnStartAction(final int uniqueId, final int actionType, final int actionId, final int fightId) {
        super(uniqueId, actionType, actionId, fightId);
    }
    
    @Override
    protected void runCore() {
        final CharacterInfo fighter = this.getFighterById(this.getInstigatorId());
        if (fighter == null) {
            FighterTurnStartAction.m_logger.error((Object)"D\u00e9but de tour demand\u00e9 pour un fighter inexistant ??");
            return;
        }
        if (!fighter.isActiveProperty(FightPropertyType.CANT_BE_DIFFERENTIATED_FROM_COPIES)) {
            final CharacterActor actor = fighter.getActor();
            actor.addActiveTeamParticleSystem(fighter.getTeamId());
        }
        if (this.consernLocalPlayer()) {
            final Fight fight = (Fight)this.getFight();
            if (!fight.askForFighterTurnBegin(fighter)) {
                FighterTurnStartAction.m_logger.error((Object)("impossible de d\u00e9buter le tour du fighter " + fighter.getId()));
                fight.askForRecoveryProcess();
            }
        }
        UIFightCameraFrame.getInstance().onFighterToPlay(fighter);
        if (WakfuGameEntity.getInstance().getLocalPlayer() == fighter) {
            ClientGameEventManager.INSTANCE.fireEvent(new ClientEventStartTurn());
        }
    }
    
    @Override
    protected void onActionFinished() {
    }
}
