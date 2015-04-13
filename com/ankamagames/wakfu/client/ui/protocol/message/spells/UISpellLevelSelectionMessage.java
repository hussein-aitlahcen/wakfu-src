package com.ankamagames.wakfu.client.ui.protocol.message.spells;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.spell.*;

public class UISpellLevelSelectionMessage extends UIMessage
{
    private SpellLevel m_spell;
    private int m_button;
    
    public SpellLevel getSpell() {
        return this.m_spell;
    }
    
    public int getButton() {
        return this.m_button;
    }
    
    public void setSpell(final SpellLevel spell) {
        this.m_spell = spell;
    }
    
    public void setButton(final int button) {
        this.m_button = button;
    }
}
