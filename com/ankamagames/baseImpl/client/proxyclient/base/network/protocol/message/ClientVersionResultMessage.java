package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import java.nio.*;

public class ClientVersionResultMessage extends InputOnlyProxyMessage
{
    private boolean m_versionMatch;
    private byte[] m_neededVersion;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_versionMatch = (bb.get() == 1);
        bb.get(this.m_neededVersion = new byte[bb.remaining()]);
        return true;
    }
    
    @Override
    public int getId() {
        return 8;
    }
    
    public boolean isVersionMatch() {
        return this.m_versionMatch;
    }
    
    public byte[] getNeededVersion() {
        return this.m_neededVersion;
    }
}
