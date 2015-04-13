package com.ankamagames.wakfu.client.ui.protocol.message.fight;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.common.game.item.*;

public class UIFighterSelectSpellOverItemMessage extends UIMessage
{
    private SpellLevel m_spell;
    private Item m_item;
    private byte m_equipmentPos;
    
    public SpellLevel getSpell() {
        return this.m_spell;
    }
    
    public void setSpell(final SpellLevel spell) {
        this.m_spell = spell;
    }
    
    public Item getItem() {
        return this.m_item;
    }
    
    public void setItem(final Item item) {
        this.m_item = item;
    }
    
    public byte getEquipmentPos() {
        return this.m_equipmentPos;
    }
    
    public void setEquipmentPos(final byte equipmentPos) {
        this.m_equipmentPos = equipmentPos;
    }
}
