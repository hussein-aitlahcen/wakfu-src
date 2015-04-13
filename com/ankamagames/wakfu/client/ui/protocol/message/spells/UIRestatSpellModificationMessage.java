package com.ankamagames.wakfu.client.ui.protocol.message.spells;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.spell.*;

public class UIRestatSpellModificationMessage extends UIMessage
{
    private RestatSpellLevel m_restatSpellLevel;
    
    public RestatSpellLevel getRestatSpellLevel() {
        return this.m_restatSpellLevel;
    }
    
    public void setRestatSpellLevel(final RestatSpellLevel restatSpellLevel) {
        this.m_restatSpellLevel = restatSpellLevel;
    }
}
