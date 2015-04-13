package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public class CharacterCreationResultMessage extends InputOnlyProxyMessage
{
    private byte m_creationCode;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        this.m_creationCode = rawDatas[0];
        return true;
    }
    
    @Override
    public int getId() {
        return 2054;
    }
    
    public boolean isCreationSuccessful() {
        return this.m_creationCode == 0;
    }
    
    public byte getCreationCode() {
        return this.m_creationCode;
    }
}
