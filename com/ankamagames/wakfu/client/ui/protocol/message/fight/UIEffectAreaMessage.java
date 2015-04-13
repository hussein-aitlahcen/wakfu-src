package com.ankamagames.wakfu.client.ui.protocol.message.fight;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.spell.*;

public class UIEffectAreaMessage extends UIMessage
{
    private EffectAreaFieldProvider m_effectArea;
    
    public UIEffectAreaMessage(final EffectAreaFieldProvider effectArea) {
        super();
        this.m_effectArea = effectArea;
    }
    
    public EffectAreaFieldProvider getEffectArea() {
        return this.m_effectArea;
    }
    
    @Override
    public int getId() {
        return 16152;
    }
}
