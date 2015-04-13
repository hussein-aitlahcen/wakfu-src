package com.ankamagames.wakfu.client.ui.protocol.message.fight;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.fight.time.*;

public class UITimePointBonusMessage extends UIMessage
{
    private TimePointEffect m_effect;
    
    public TimePointEffect getEffect() {
        return this.m_effect;
    }
    
    public void setEffect(final TimePointEffect effect) {
        this.m_effect = effect;
    }
}
