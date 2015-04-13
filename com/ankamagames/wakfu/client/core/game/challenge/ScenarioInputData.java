package com.ankamagames.wakfu.client.core.game.challenge;

public class ScenarioInputData
{
    private int m_id;
    private byte m_varId;
    
    public ScenarioInputData(final int id, final byte varId) {
        super();
        this.m_id = id;
        this.m_varId = varId;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public byte getVarId() {
        return this.m_varId;
    }
}
