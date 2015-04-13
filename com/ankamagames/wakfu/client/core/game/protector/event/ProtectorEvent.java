package com.ankamagames.wakfu.client.core.game.protector.event;

import com.ankamagames.wakfu.common.game.protector.event.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;

public abstract class ProtectorEvent extends com.ankamagames.wakfu.common.game.protector.event.ProtectorEvent<Protector>
{
    private int m_id;
    
    protected void setId(final int id) {
        this.m_id = id;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public abstract ProtectorMood getProtectorMood();
    
    public String[] getParams() {
        return null;
    }
}
