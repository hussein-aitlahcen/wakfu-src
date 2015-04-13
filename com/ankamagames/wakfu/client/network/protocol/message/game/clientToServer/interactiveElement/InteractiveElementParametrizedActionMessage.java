package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.interactiveElement;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.interactiveElements.action.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public final class InteractiveElementParametrizedActionMessage extends OutputOnlyProxyMessage
{
    private long m_elementId;
    private InteractiveElementParametrizedAction m_action;
    
    @Override
    public byte[] encode() {
        final byte[] action = this.m_action.serialize();
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_elementId);
        ba.putShort(this.m_action.getId());
        ba.put(action);
        return this.addClientHeader((byte)3, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 10123;
    }
    
    public void setElementId(final long elementId) {
        this.m_elementId = elementId;
    }
    
    public void setAction(final InteractiveElementParametrizedAction action) {
        this.m_action = action;
    }
}
