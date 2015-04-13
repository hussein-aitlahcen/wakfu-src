package com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public interface ClientInteractiveElementView extends Releasable, InteractiveElementChangesListener
{
    long getId();
    
    int getViewModelId();
    
    byte getViewHeight();
    
    void setViewModelId(int p0);
    
    void setViewGfxId(int p0);
    
    void setViewHeight(byte p0);
    
    void setBehindMobile(boolean p0);
    
    void setColor(int p0);
    
    void setParticleSystemId(int p0, int p1);
    
    ClientMapInteractiveElement getInteractiveElement();
    
    void setInteractiveElement(ClientMapInteractiveElement p0);
    
    void update();
}
