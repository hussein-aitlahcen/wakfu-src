package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class FighterTurnStartEvent extends FighterTurnEvent implements Releasable
{
    private static final MonitoredPool m_staticPool;
    private static Logger m_logger;
    
    @Override
    public void sendTo(final TimeEventHandler handler) {
        handler.handleFighterTurnStartEvent(this);
    }
    
    public static FighterTurnStartEvent checkOut(final long fighterId) {
        FighterTurnStartEvent event;
        try {
            event = (FighterTurnStartEvent)FighterTurnStartEvent.m_staticPool.borrowObject();
            event.m_pool = FighterTurnStartEvent.m_staticPool;
        }
        catch (Exception e) {
            FighterTurnStartEvent.m_logger.warn((Object)("Erreur au checkOut d'un " + FighterTurnStartEvent.class.getSimpleName()));
            event = new FighterTurnStartEvent();
        }
        event.setFighterId(fighterId);
        return event;
    }
    
    @Override
    public void release() {
        if (this.m_pool != null) {
            try {
                this.m_pool.returnObject(this);
            }
            catch (Exception e) {
                FighterTurnStartEvent.m_logger.warn((Object)("Erreur au release d'un " + FighterTurnStartEvent.class.getSimpleName()));
            }
            this.m_pool = null;
        }
        else {
            FighterTurnStartEvent.m_logger.error((Object)("Double release de " + this.getClass().toString()));
        }
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<FighterTurnStartEvent>() {
            @Override
            public FighterTurnStartEvent makeObject() {
                return new FighterTurnStartEvent();
            }
        });
        FighterTurnStartEvent.m_logger = Logger.getLogger((Class)FighterTurnStartEvent.class);
    }
}
