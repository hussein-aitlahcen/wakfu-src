package com.ankamagames.framework.kernel.events;

import com.ankamagames.framework.kernel.core.net.netty.*;

public interface NetworkEventsHandler
{
    boolean onNewConnection(ConnectionCtx p0);
    
    boolean onConnectionClose(ConnectionCtx p0);
}
