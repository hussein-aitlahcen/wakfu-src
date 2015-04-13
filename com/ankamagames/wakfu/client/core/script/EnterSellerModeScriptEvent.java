package com.ankamagames.wakfu.client.core.script;

import com.ankamagames.framework.script.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.script.*;

public class EnterSellerModeScriptEvent extends FilterableEvent
{
    protected static final Logger m_logger;
    
    @Override
    public short getId() {
        return 5;
    }
    
    @Override
    public LuaTable getInfoForLUA() {
        return null;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object object) {
        return object instanceof EnterSellerModeScriptEvent;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EnterSellerModeScriptEvent.class);
    }
}
