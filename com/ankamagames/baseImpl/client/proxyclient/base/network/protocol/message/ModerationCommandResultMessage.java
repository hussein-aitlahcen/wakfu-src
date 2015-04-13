package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import java.nio.*;
import com.ankamagames.framework.kernel.utils.*;

public class ModerationCommandResultMessage extends InputOnlyProxyMessage
{
    public static final byte TRACE = 0;
    public static final byte LOG = 1;
    public static final byte ERROR = 2;
    public static final byte CUSTOM = 3;
    private byte m_messageType;
    private String m_message;
    private int m_color;
    
    public ModerationCommandResultMessage() {
        super();
        this.m_message = null;
        this.m_color = 16777215;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (!this.checkMessageSize(rawDatas.length, 3, false)) {
            return false;
        }
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_messageType = bb.get();
        if (this.m_messageType == 3) {
            this.m_color = bb.getInt();
        }
        final Short size = bb.getShort();
        final byte[] str = new byte[size & 0xFFFF];
        bb.get(str);
        this.m_message = StringUtils.fromUTF8(str);
        return true;
    }
    
    @Override
    public int getId() {
        return 105;
    }
    
    public byte getMessageType() {
        return this.m_messageType;
    }
    
    public String getMessage() {
        return this.m_message;
    }
    
    public int getColor() {
        return this.m_color;
    }
}
