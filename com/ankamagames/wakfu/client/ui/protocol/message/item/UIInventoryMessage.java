package com.ankamagames.wakfu.client.ui.protocol.message.item;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.item.*;

public class UIInventoryMessage extends UIMessage
{
    private Bag m_newContainer;
    private Bag m_oldContainer;
    private String m_parentWindowId;
    
    public UIInventoryMessage() {
        super();
        this.m_parentWindowId = null;
    }
    
    public Bag getNewContainer() {
        return this.m_newContainer;
    }
    
    public void setNewContainer(final Bag container) {
        this.m_newContainer = container;
    }
    
    public String getParentWindowId() {
        return this.m_parentWindowId;
    }
    
    public void setParentWindowId(final String parentWindowId) {
        this.m_parentWindowId = parentWindowId;
    }
    
    public Bag getOldContainer() {
        return this.m_oldContainer;
    }
    
    public void setOldContainer(final Bag oldContainer) {
        this.m_oldContainer = oldContainer;
    }
}
