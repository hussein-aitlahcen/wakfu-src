package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fight.time.timescore.*;

public class PointEffectSelectedAction extends TimedAction
{
    private static final Logger m_logger;
    private Fight m_fight;
    private final long m_chooserId;
    private final int m_effectId;
    
    public PointEffectSelectedAction(final int uniqueId, final int actionType, final int actionId, final Fight fight, final long chooserId, final int effectId) {
        super(uniqueId, actionType, actionId);
        this.m_fight = fight;
        this.m_chooserId = chooserId;
        this.m_effectId = effectId;
    }
    
    @Override
    protected long onRun() {
        final TimeScoreGauges timeScoreGauges = this.m_fight.getTimeline().getTimeScoreGauges();
        timeScoreGauges.resetRemainingSeconds(this.m_chooserId);
        final CharacterInfo fighter = this.m_fight.getFighterFromId(this.m_chooserId);
        timeScoreGauges.selectEffect(this.m_chooserId, this.m_effectId, this.m_fight.getOriginalTeamId(fighter));
        timeScoreGauges.removeEffectToBeApplied(this.m_chooserId);
        UITimePointSelectionFrame.getInstance().displayEffectSelected(this.m_effectId, false);
        if (WakfuGameEntity.getInstance().getLocalPlayer().getId() == this.m_chooserId) {
            return 0L;
        }
        return 1500L;
    }
    
    @Override
    protected void onActionFinished() {
    }
    
    public Fight getFight() {
        return this.m_fight;
    }
    
    public void setFight(final Fight fight) {
        this.m_fight = fight;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PointEffectSelectedAction.class);
    }
}
