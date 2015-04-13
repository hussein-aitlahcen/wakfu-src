package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.utils.*;

public class ClientCreateSteamAccountMessage extends OutputOnlyProxyMessage
{
    private long m_steamId;
    private String m_language;
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_steamId);
        final byte[] utfLanguage = StringUtils.toUTF8(this.m_language);
        ba.putInt(utfLanguage.length);
        ba.put(utfLanguage);
        return this.addClientHeader((byte)8, ba.toArray());
    }
    
    public void setSteamId(final long steamId) {
        this.m_steamId = steamId;
    }
    
    @Override
    public int getId() {
        return 1044;
    }
    
    public void setLanguage(final String language) {
        this.m_language = language;
    }
}
