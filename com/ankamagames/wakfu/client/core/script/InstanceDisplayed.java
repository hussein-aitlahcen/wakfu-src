package com.ankamagames.wakfu.client.core.script;

import com.ankamagames.framework.script.events.*;
import com.ankamagames.framework.script.*;

public class InstanceDisplayed extends FilterableEvent
{
    @Override
    public short getId() {
        return 0;
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
        return object instanceof InstanceDisplayed;
    }
}
