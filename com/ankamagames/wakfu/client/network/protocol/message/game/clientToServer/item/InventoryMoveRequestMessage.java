package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public abstract class InventoryMoveRequestMessage extends OutputOnlyProxyMessage
{
    protected long m_uid;
    protected long m_destinationId;
    protected long m_source;
    protected long m_newuid;
    
    @Override
    public abstract byte[] encode();
    
    @Override
    public abstract int getId();
    
    public void setItemUId(final long uid) {
        this.m_uid = uid;
    }
    
    public void setDestination(final long dest) {
        this.m_destinationId = dest;
    }
    
    public void setNewUid(final long id) {
        this.m_newuid = id;
    }
    
    public void setSource(final long source) {
        this.m_source = source;
    }
}
