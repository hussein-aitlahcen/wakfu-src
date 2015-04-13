package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class NewTableTurnAction extends AbstractFightAction
{
    private final byte[] m_shortTimelineSerialisation;
    
    public NewTableTurnAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final byte[] shortTimelineSerialisation) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_shortTimelineSerialisation = shortTimelineSerialisation;
    }
    
    @Override
    protected void runCore() {
        final Fight fight = (Fight)this.getFight();
        if (fight == null) {
            NewTableTurnAction.m_logger.error((Object)"Impossible de debuter un nouvezau tour de table, on ne connait pas le combat ");
            return;
        }
        if (this.consernLocalPlayer()) {
            fight.getTimeline().askForStartTurn();
        }
        UIFightCameraFrame.getInstance().onNewTableTurn();
    }
    
    @Override
    protected void onActionFinished() {
    }
}
