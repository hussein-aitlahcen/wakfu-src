package com.ankamagames.baseImpl.client.proxyclient.base.game.achievements;

import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;

public final class Variable implements AchievementVariable
{
    private final int m_id;
    private final String m_name;
    private long m_value;
    private boolean m_exportForSteam;
    
    Variable(final Variable variable) {
        super();
        this.m_id = variable.m_id;
        this.m_name = variable.m_name;
        this.m_exportForSteam = variable.m_exportForSteam;
    }
    
    Variable(final int id, final String name, final boolean exportForSteam) {
        super();
        this.m_id = id;
        this.m_name = ((name != null) ? name.intern() : null);
        this.m_exportForSteam = exportForSteam;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public long getValue() {
        return this.m_value;
    }
    
    public boolean isExportForSteam() {
        return this.m_exportForSteam;
    }
    
    void setValue(final long value) {
        this.m_value = value;
    }
}
