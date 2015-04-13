package com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class WakfuAuthenticationTokenResultMessage extends InputOnlyProxyMessage
{
    private byte m_resultCode;
    private String m_token;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 1, false)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_resultCode = bb.get();
        final byte[] utfNickName = new byte[bb.getInt()];
        bb.get(utfNickName);
        this.m_token = StringUtils.fromUTF8(utfNickName);
        return true;
    }
    
    @Override
    public int getId() {
        return 1212;
    }
    
    public byte getErrorCode() {
        return this.m_resultCode;
    }
    
    public String getToken() {
        return this.m_token;
    }
}
