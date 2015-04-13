package com.ankamagames.framework.script.events;

import com.ankamagames.framework.script.*;

public abstract class FilterableEvent
{
    public abstract short getId();
    
    @Override
    public abstract int hashCode();
    
    public abstract LuaTable getInfoForLUA();
    
    @Override
    public abstract boolean equals(final Object p0);
}
