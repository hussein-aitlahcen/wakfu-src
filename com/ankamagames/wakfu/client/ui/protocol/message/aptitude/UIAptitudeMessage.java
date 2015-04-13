package com.ankamagames.wakfu.client.ui.protocol.message.aptitude;

import com.ankamagames.wakfu.common.game.aptitude.*;

public class UIAptitudeMessage extends UIAptitudeCommonMessage
{
    private Aptitude m_aptitude;
    
    public Aptitude getAptitude() {
        return this.m_aptitude;
    }
    
    public void setAptitude(final Aptitude aptitude) {
        this.m_aptitude = aptitude;
    }
}
