package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;

public final class PointEffectSelectionActivationAction extends TimedAction
{
    private static final Logger m_logger;
    private CharacterInfo m_fighter;
    private final long m_remainingMillis;
    
    public PointEffectSelectionActivationAction(final int uniqueId, final int actionType, final int actionId, final CharacterInfo fighter, final long remainingMillis) {
        super(uniqueId, actionType, actionId);
        this.m_fighter = fighter;
        this.m_remainingMillis = remainingMillis;
    }
    
    @Override
    protected long onRun() {
        UITimePointSelectionFrame.getInstance().setFighter(this.m_fighter);
        UITimePointSelectionFrame.getInstance().setRemainingMillis((this.m_remainingMillis > 0L) ? this.m_remainingMillis : 30000L);
        WakfuGameEntity.getInstance().pushFrame(UITimePointSelectionFrame.getInstance());
        return 500L;
    }
    
    @Override
    protected void onActionFinished() {
    }
    
    static {
        m_logger = Logger.getLogger((Class)PointEffectSelectionActivationAction.class);
    }
}
