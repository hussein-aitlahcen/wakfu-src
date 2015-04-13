package com.ankamagames.wakfu.client.network.protocol.frame.fight;

import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import java.util.*;
import com.ankamagames.framework.kernel.*;

public class FightManagementFrame<F extends FightInfo> implements MessageFrame
{
    private static Logger m_logger;
    protected final List<FightMessageHandler<? extends Message, F>> m_handlers;
    
    public FightManagementFrame() {
        super();
        this.m_handlers = new ArrayList<FightMessageHandler<? extends Message, F>>();
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final int id = message.getId();
        for (final FightMessageHandler handler : this.m_handlers) {
            if (id == handler.getHandledMessageId()) {
                try {
                    final boolean forwardMessage = handler.onMessage(message);
                    if (forwardMessage) {
                        continue;
                    }
                    return forwardMessage;
                }
                catch (Exception e) {
                    FightManagementFrame.m_logger.error((Object)"Exception levee", (Throwable)e);
                }
            }
        }
        return true;
    }
    
    public void addHandler(final FightMessageHandler<? extends Message, F> handler) {
        if (!this.m_handlers.contains(handler)) {
            this.m_handlers.add(handler);
        }
    }
    
    public boolean removeHandler(final FightMessageHandler handler) {
        return this.m_handlers.remove(handler);
    }
    
    boolean containsHandler(final FightMessageHandler<? extends Message, F> o) {
        return this.m_handlers.contains(o);
    }
    
    public void associateFight(final F fight) {
        for (final FightMessageHandler<? extends Message, F> handler : this.m_handlers) {
            handler.setConcernedFight(fight);
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    static {
        FightManagementFrame.m_logger = Logger.getLogger((Class)FightManagementFrame.class);
    }
}
