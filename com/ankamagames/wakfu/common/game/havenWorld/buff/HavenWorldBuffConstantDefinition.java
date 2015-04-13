package com.ankamagames.wakfu.common.game.havenWorld.buff;

import com.ankamagames.baseImpl.common.clientAndServer.utils.*;

public final class HavenWorldBuffConstantDefinition extends ConstantDefinition<HavenWorldBuffDefinition>
{
    private String m_adminDescription;
    
    public HavenWorldBuffConstantDefinition(final HavenWorldBuffDefinition object, final int id, final Constants<HavenWorldBuffDefinition> actionConstants, final String description) {
        super(id, object, actionConstants);
        this.m_adminDescription = description;
    }
    
    @Override
    public String getAdminDescription() {
        return this.m_adminDescription;
    }
}
