package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;

public class TempInventoryClearRequestMessage extends OutputOnlyProxyMessage
{
    protected static final Logger m_logger;
    
    @Override
    public byte[] encode() {
        return this.addClientHeader((byte)3, new byte[0]);
    }
    
    @Override
    public int getId() {
        return 5217;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TempInventoryClearRequestMessage.class);
    }
}
