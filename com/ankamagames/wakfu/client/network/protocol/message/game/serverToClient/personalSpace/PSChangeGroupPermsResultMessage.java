package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;

public class PSChangeGroupPermsResultMessage extends InputOnlyProxyMessage
{
    private byte m_resultCode;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        this.m_resultCode = rawDatas[0];
        return true;
    }
    
    public byte getResultCode() {
        return this.m_resultCode;
    }
    
    @Override
    public int getId() {
        return 10014;
    }
}
