package com.ankamagames.wakfu.client.ui.protocol.message.spells;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.spell.*;

public class UISpellLevelMessage extends UIMessage
{
    private SpellLevel m_spell;
    
    public SpellLevel getSpell() {
        return this.m_spell;
    }
    
    public void setSpell(final SpellLevel spell) {
        this.m_spell = spell;
    }
}
