package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class ConsoleAdminCommandResultMessage extends InputOnlyProxyMessage
{
    public static final byte TRACE = 0;
    public static final byte LOG = 1;
    public static final byte ERROR = 2;
    private byte m_messageType;
    private String m_message;
    
    public ConsoleAdminCommandResultMessage() {
        super();
        this.m_message = null;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 3, false)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_messageType = bb.get();
        final Short size = bb.getShort();
        final byte[] str = new byte[size & 0xFFFF];
        bb.get(str);
        this.m_message = StringUtils.fromUTF8(str);
        return true;
    }
    
    @Override
    public int getId() {
        return 102;
    }
    
    public byte getMessageType() {
        return this.m_messageType;
    }
    
    public String getMessage() {
        return this.m_message;
    }
}
