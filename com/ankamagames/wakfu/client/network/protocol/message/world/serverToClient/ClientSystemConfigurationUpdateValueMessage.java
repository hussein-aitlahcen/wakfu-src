package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public final class ClientSystemConfigurationUpdateValueMessage extends InputOnlyProxyMessage
{
    private String m_key;
    private String m_value;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_key = this.getString(bb);
        this.m_value = this.getString(bb);
        return false;
    }
    
    public String getKey() {
        return this.m_key;
    }
    
    public String getValue() {
        return this.m_value;
    }
    
    private String getString(final ByteBuffer bb) {
        final byte[] bytes = new byte[bb.getInt()];
        bb.get(bytes);
        return StringUtils.fromUTF8(bytes);
    }
    
    @Override
    public int getId() {
        return 2081;
    }
}
