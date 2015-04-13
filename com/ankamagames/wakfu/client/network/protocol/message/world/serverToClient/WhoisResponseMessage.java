package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public final class WhoisResponseMessage extends InputOnlyProxyMessage
{
    public static final byte NORMAL_STATUS = 0;
    public static final byte UNKNOWN_CHARACTER_STATUS = 1;
    private String m_message;
    private byte m_statusCode;
    
    public WhoisResponseMessage() {
        super();
        this.m_statusCode = -1;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_statusCode = bb.get();
        final short size = bb.getShort();
        final byte[] mdata = new byte[size];
        bb.get(mdata);
        this.m_message = StringUtils.fromUTF8(mdata);
        return true;
    }
    
    @Override
    public int getId() {
        return 2061;
    }
    
    public String getMessage() {
        return this.m_message;
    }
    
    public byte getStatusCode() {
        return this.m_statusCode;
    }
}
