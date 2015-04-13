package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public class CharacterChoiceLeaveRequestMessage extends OutputOnlyProxyMessage
{
    private static final byte[] EMPTY_ARRAY;
    
    @Override
    public byte[] encode() {
        return this.addClientHeader((byte)2, CharacterChoiceLeaveRequestMessage.EMPTY_ARRAY);
    }
    
    @Override
    public int getId() {
        return 2055;
    }
    
    static {
        EMPTY_ARRAY = new byte[0];
    }
}
