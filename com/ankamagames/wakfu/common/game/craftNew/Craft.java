package com.ankamagames.wakfu.common.game.craftNew;

import com.ankamagames.wakfu.common.game.craftNew.util.*;

public class Craft
{
    private final int m_referenceId;
    private long m_xp;
    private boolean m_contractEnabled;
    
    public Craft(final int referenceId) {
        super();
        this.m_referenceId = referenceId;
    }
    
    public int getReferenceId() {
        return this.m_referenceId;
    }
    
    public long getXp() {
        return this.m_xp;
    }
    
    public void setXp(final long xp) {
        this.m_xp = xp;
    }
    
    public boolean isContractEnabled() {
        return this.m_contractEnabled;
    }
    
    public void setContractEnabled(final boolean contractEnabled) {
        this.m_contractEnabled = contractEnabled;
    }
    
    public void addXp(final long xp) {
        this.m_xp += xp;
    }
    
    public short getLevel() {
        return CraftXPUtil.getCraftLevelForXp(this.m_xp);
    }
    
    public byte[] serializeCraft() {
        return CraftSerializer.serializeCraft(this);
    }
}
