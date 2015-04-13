package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;

public class UIFightEndTurnFrame implements MessageFrame
{
    private static UIFightEndTurnFrame m_instance;
    
    public static UIFightEndTurnFrame getInstance() {
        return UIFightEndTurnFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        message.getId();
        return true;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        PropertiesProvider.getInstance().setPropertyValue("isInFightPlacement", WakfuGameEntity.getInstance().hasFrame(UIFightPlacementFrame.getInstance()));
        PropertiesProvider.getInstance().setPropertyValue("isInFightPlayerTurn", true);
    }
    
    private void cleanUpAps(final String elementMapId) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(elementMapId);
        if (map != null) {
            final Widget container = (Widget)map.getElement("apsContainer");
            container.setVisible(false);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.cleanUpAps("worldAndFightBarDialog");
            this.applyTimePointBonusParticle();
            PropertiesProvider.getInstance().setPropertyValue("isInFightPlayerTurn", false);
        }
    }
    
    private void applyTimePointBonusParticle() {
    }
    
    static {
        UIFightEndTurnFrame.m_instance = new UIFightEndTurnFrame();
    }
}
