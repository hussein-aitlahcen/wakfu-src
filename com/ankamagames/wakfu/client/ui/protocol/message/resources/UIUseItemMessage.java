package com.ankamagames.wakfu.client.ui.protocol.message.resources;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.*;

public class UIUseItemMessage extends UIMessage
{
    private final Item m_item;
    private final byte m_equipmentPos;
    private final boolean m_onSelf;
    
    public UIUseItemMessage(final Item item, final byte equimentPos, final boolean onSelf) {
        super();
        this.m_item = item;
        this.m_equipmentPos = equimentPos;
        this.m_onSelf = onSelf;
    }
    
    public Item getItem() {
        return this.m_item;
    }
    
    public byte getEquipmentPos() {
        return this.m_equipmentPos;
    }
    
    public boolean isOnSelf() {
        return this.m_onSelf;
    }
}
