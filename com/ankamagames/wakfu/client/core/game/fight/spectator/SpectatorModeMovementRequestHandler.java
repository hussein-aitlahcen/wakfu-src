package com.ankamagames.wakfu.client.core.game.fight.spectator;

import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.xulor2.core.messagebox.*;

public final class SpectatorModeMovementRequestHandler
{
    private boolean m_isMessageBoxOpened;
    
    public void movementRequest() {
        if (this.m_isMessageBoxOpened) {
            return;
        }
        final MessageBoxControler controller = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("spectator.mode.movement"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 4102L, 102, 1);
        controller.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 2) {
                    final Fight observedFight = WakfuGameEntity.getInstance().getLocalPlayer().getObservedFight();
                    FightActionGroupManager.getInstance().addActionToPendingGroup(observedFight, new Action(TimedAction.getNextUid(), FightActionType.LEAVE_SPECTATOR_MODE.getId(), 0) {
                        @Override
                        public void run() {
                            final LeaveSpectatorModeProcedure leaveSpectatorModeProcedure = new LeaveSpectatorModeProcedure();
                            leaveSpectatorModeProcedure.execute();
                            this.fireActionFinishedEvent();
                        }
                        
                        @Override
                        protected void onActionFinished() {
                        }
                    });
                    FightActionGroupManager.getInstance().executePendingGroup(observedFight);
                }
                SpectatorModeMovementRequestHandler.this.m_isMessageBoxOpened = false;
            }
        });
        this.m_isMessageBoxOpened = true;
    }
}
