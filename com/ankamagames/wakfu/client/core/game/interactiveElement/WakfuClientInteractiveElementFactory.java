package com.ankamagames.wakfu.client.core.game.interactiveElement;

import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public final class WakfuClientInteractiveElementFactory extends ClientInteractiveElementFactory<WakfuClientMapInteractiveElement, WakfuClientInteractiveElementFactoryConfiguration>
{
    private static final WakfuClientInteractiveElementFactory m_instance;
    
    public static WakfuClientInteractiveElementFactory getInstance() {
        return WakfuClientInteractiveElementFactory.m_instance;
    }
    
    @Override
    protected void setAdditionalData(final WakfuClientMapInteractiveElement element, final InteractiveElementInfo info) {
        super.setAdditionalData(element, info);
        element.onStateChanged();
    }
    
    public WakfuClientMapInteractiveElement createDummyInteractiveElement(final long instanceId) {
        return ((InteractiveElementFactory<WakfuClientMapInteractiveElement, C>)this).createInteractiveElement(instanceId, true);
    }
    
    static {
        m_instance = new WakfuClientInteractiveElementFactory();
    }
}
