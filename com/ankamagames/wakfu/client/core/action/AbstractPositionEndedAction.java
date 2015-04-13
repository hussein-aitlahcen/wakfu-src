package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public abstract class AbstractPositionEndedAction extends TimedAction implements MobileEndPathListener
{
    private static final Logger m_logger;
    
    protected AbstractPositionEndedAction(final int uniqueId, final int actionType, final int actionId) {
        super(uniqueId, actionType, actionId);
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        MessageScheduler.getInstance().removeClock(this.m_clockId);
        this.fireActionFinishedEvent();
    }
    
    @Override
    public boolean onMessage(final Message message) {
        AbstractPositionEndedAction.m_logger.warn((Object)"le personnage n'est pas encore arriv\u00e9 a destination, le timer termine l'action.");
        return super.onMessage(message);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractPositionEndedAction.class);
    }
}
