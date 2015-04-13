package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.client.proxyclient.base.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;

public class NetForwardToGameEntityFrame implements MessageFrame
{
    private ProxyClientEntity m_proxyClientEntity;
    
    public NetForwardToGameEntityFrame(final ProxyClientEntity proxyClientEntity) {
        super();
        this.m_proxyClientEntity = proxyClientEntity;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        return this.m_proxyClientEntity.onMessage(message);
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
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
}
