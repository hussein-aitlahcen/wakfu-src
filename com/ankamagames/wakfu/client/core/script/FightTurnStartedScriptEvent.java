package com.ankamagames.wakfu.client.core.script;

import com.ankamagames.framework.script.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.script.*;

public class FightTurnStartedScriptEvent extends FilterableEvent
{
    protected static final Logger m_logger;
    private Long m_characterId;
    
    public FightTurnStartedScriptEvent(final long characterId) {
        super();
        this.m_characterId = characterId;
    }
    
    @Override
    public short getId() {
        return 6;
    }
    
    @Override
    public LuaTable getInfoForLUA() {
        return null;
    }
    
    @Override
    public int hashCode() {
        return this.m_characterId.hashCode();
    }
    
    @Override
    public boolean equals(final Object object) {
        return object instanceof FightTurnStartedScriptEvent && ((FightTurnStartedScriptEvent)object).m_characterId.equals(this.m_characterId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)FightTurnStartedScriptEvent.class);
    }
}
