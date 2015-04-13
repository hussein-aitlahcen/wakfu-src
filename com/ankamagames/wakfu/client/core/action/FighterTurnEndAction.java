package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.fight.time.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public class FighterTurnEndAction extends AbstractFightAction
{
    private final int m_timeScoreGain;
    private int m_addedRemainingSeconds;
    
    public FighterTurnEndAction(final int uniqueId, final int actionType, final int actionId, final int timeScoreGain, final int fightId) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_timeScoreGain = timeScoreGain;
    }
    
    @Override
    protected void runCore() {
        if (!this.consernLocalPlayer()) {
            return;
        }
        final Fight fight = (Fight)this.getFight();
        final Timeline timeline = fight.getTimeline();
        final CharacterInfo fighter = timeline.getCurrentFighter();
        final long fighterId = this.getInstigatorId();
        if (WakfuGameEntity.getInstance().hasFrame(UITimePointSelectionFrame.getInstance()) && UITimePointSelectionFrame.getInstance().getFighter().getId() == fighterId) {
            WakfuGameEntity.getInstance().removeFrame(UITimePointSelectionFrame.getInstance());
        }
        this.endFighterTurn(fight, timeline, fighter, fighterId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new FighterTurnEndAckMessage(timeline.getTurnCounter()));
    }
    
    private void endFighterTurn(final Fight fight, final Timeline timeline, final CharacterInfo fighter, final long fighterId) {
        if (fighter == null) {
            if (!timeline.askForFighterEndTurn(fighterId)) {
                FighterTurnEndAction.m_logger.error((Object)("impossible de terminer le tour du fighter " + fighterId));
                fight.askForRecoveryProcess();
            }
            return;
        }
        if (fighter.getId() != this.getInstigatorId()) {
            FighterTurnEndAction.m_logger.info((Object)"fin de tour pr\u00e9matur\u00e9e du client");
            return;
        }
        timeline.getTimeScoreGauges().cancelFighterChoosePermission(fighterId);
        timeline.getTimeScoreGauges().updateTimeScore(fighterId, this.m_timeScoreGain);
        timeline.getTimeScoreGauges().addTurnRemainingSeconds(fighterId, this.m_addedRemainingSeconds);
        final CharacterActor actor = fighter.getActor();
        if (fight.isInPlay(fighter)) {
            actor.addPassiveTeamParticleSystem(fighter.getTeamId());
        }
        if (!fight.askForFighterTurnEnd(fighter)) {
            FighterTurnEndAction.m_logger.error((Object)("impossible de terminer le tour du fighter " + fighter.getId()));
            fight.askForRecoveryProcess();
        }
    }
    
    @Override
    protected void onActionFinished() {
    }
    
    public void setAddedRemainingSeconds(final int addedRemainingSeconds) {
        this.m_addedRemainingSeconds = addedRemainingSeconds;
    }
}
