package com.ankamagames.wakfu.common.game.item.bind;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public class ItemBindModel implements ItemBind
{
    private ItemBindType m_type;
    private long m_data;
    
    public ItemBindModel(final ItemBindType type) {
        super();
        this.m_type = type;
    }
    
    public ItemBindModel(final ItemBindType type, final long data) {
        super();
        this.m_type = type;
        this.m_data = data;
    }
    
    @Override
    public ItemBindType getType() {
        return this.m_type;
    }
    
    @Override
    public long getData() {
        return this.m_data;
    }
    
    public void setData(final long data) {
        this.m_data = data;
    }
    
    @Override
    public void toRaw(final RawItemBind raw) {
        raw.data = this.m_data;
        raw.type = this.m_type.getId();
    }
    
    @Override
    public void fromRaw(final RawItemBind raw) {
        this.m_data = raw.data;
        this.m_type = ItemBindType.getFromId(raw.type);
    }
    
    @Override
    public ItemBind getCopy() {
        return new ItemBindModel(this.m_type, this.m_data);
    }
    
    @Override
    public boolean equals(final ItemBind bind) {
        return bind != null && bind.getType() == this.m_type && bind.getData() == this.m_data;
    }
}
