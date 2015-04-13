package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.framework.kernel.utils.*;

public class ClientDispatchNickNameMessage extends OutputOnlyProxyMessage
{
    private String m_nickName;
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        final byte[] utfNickName = StringUtils.toUTF8(this.m_nickName);
        ba.putInt(utfNickName.length);
        ba.put(utfNickName);
        return this.addClientHeader((byte)8, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 1040;
    }
    
    public void setNickName(final String nickName) {
        this.m_nickName = nickName;
    }
}
