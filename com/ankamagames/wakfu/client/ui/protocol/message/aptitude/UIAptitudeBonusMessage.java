package com.ankamagames.wakfu.client.ui.protocol.message.aptitude;

import com.ankamagames.wakfu.client.core.game.aptitudenew.*;

public class UIAptitudeBonusMessage extends UIAptitudeCommonMessage
{
    private final AptitudeBonusView m_view;
    
    public UIAptitudeBonusMessage(final AptitudeBonusView view) {
        super();
        this.m_view = view;
    }
    
    public AptitudeBonusView getView() {
        return this.m_view;
    }
}
