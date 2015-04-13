package com.ankamagames.wakfu.client.ui.protocol.message.fight;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.*;

public class UIFighterSelectAttackMessage extends UIMessage
{
    private Item m_item;
    private byte m_equipmentPos;
    
    public Item getItem() {
        return this.m_item;
    }
    
    public byte getEquipmentPos() {
        return this.m_equipmentPos;
    }
    
    public void setItem(final Item item, final byte equimentPos) {
        this.m_item = item;
        this.m_equipmentPos = equimentPos;
    }
}
