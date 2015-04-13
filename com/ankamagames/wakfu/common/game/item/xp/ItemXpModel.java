package com.ankamagames.wakfu.common.game.item.xp;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.game.xp.*;

class ItemXpModel implements ItemXp
{
    private ItemXpDefinition m_definition;
    private long m_xp;
    
    ItemXpModel() {
        super();
    }
    
    ItemXpModel(final ItemXpDefinition definition) {
        super();
        this.m_definition = definition;
    }
    
    @Override
    public ItemXpDefinition getDefinition() {
        return this.m_definition;
    }
    
    public void setXp(final long xp) {
        this.m_xp = xp;
    }
    
    @Override
    public long getXp() {
        return this.m_xp;
    }
    
    @Override
    public short getLevel() {
        return this.getXpTable().getLevelByXp(this.m_xp);
    }
    
    @Override
    public short getMaxLevel() {
        return this.getXpTable().getMaxLevel();
    }
    
    @Override
    public float getCurrentPercentage() {
        return this.getXpTable().getPercentageInLevel(this.getLevel(), this.m_xp);
    }
    
    @Override
    public void addListener(final ItemXpModelListener listener) {
    }
    
    @Override
    public void removeListener(final ItemXpModelListener listener) {
    }
    
    @Override
    public boolean toRaw(final RawItemXp xp) {
        xp.definitionId = this.m_definition.getId();
        xp.xp = this.m_xp;
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawItemXp xp) {
        this.m_definition = ItemXpDefinitionManager.INSTANCE.get(xp.definitionId);
        this.m_xp = xp.xp;
        return true;
    }
    
    @Override
    public XpTable getXpTable() {
        if (this.m_definition != null && this.m_definition.getId() == 2) {
            return OneXpOneLevelItemXpTable.getInstance();
        }
        return ItemXpTable.getInstance();
    }
    
    @Override
    public String getLogRepresentation() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("xp(").append(this.m_xp).append(")");
        return buffer.toString();
    }
}
